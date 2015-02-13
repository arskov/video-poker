package com.arsenyko.service;

import java.util.Map;

import org.apache.commons.lang.SerializationUtils;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
/**
 * Very simplified model persistence :)
 * 
 * @author Arseny Kovalchuk
 *
 */
public class StoreServiceImpl implements StoreService {
    
    private static final String CREDIT_MODEL_KIND = "UserCredit";
    private static final String GAME_MODEL_KIND = "GameModel";
    private static final String GAME_MODEL_BLOB_PROP = "gameModelBlob";
    
    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    
    /**
     * Returns null if not found!
     */
    @Override
    public GameModel getGameModel(String userId) {
        //NamespaceManager.set(userId);
        Key key = KeyFactory.createKey(GAME_MODEL_KIND, "gameModel_" + userId);
        Entity gameModelEntity = null;
        try {
            gameModelEntity = datastore.get(key);
        } catch (EntityNotFoundException e) {
            return null;
        }
        return entityToGameModel(gameModelEntity);
    }


    @Override
    public void storeGameModel(GameModel model, String userId) {
        //NamespaceManager.set(userId);
        Key key = KeyFactory.createKey(GAME_MODEL_KIND, "gameModel_" + userId);
        datastore.put(gameModelToEntity(model, key));
    }

    private GameModel entityToGameModel(Entity gameModelEntity) {
        if (gameModelEntity == null) {
            return null;
        }
        Blob gameModelBlob = (Blob) gameModelEntity.getProperty(GAME_MODEL_BLOB_PROP);
        GameModel model =  (GameModel) SerializationUtils.deserialize(gameModelBlob.getBytes());
        return model;
    }
    
    private Entity gameModelToEntity(GameModel model, Key key) {
        Entity entity = new Entity(key);
        Blob gameModelBlob = new Blob(SerializationUtils.serialize(model));
        entity.setUnindexedProperty(GAME_MODEL_BLOB_PROP, gameModelBlob);
        return entity;
    }


    @Override
    public void reset(String userId) {
        Key key = KeyFactory.createKey(GAME_MODEL_KIND, "gameModel_" + userId);
        datastore.delete(key);
        // TODO : delete credit history
    }


    @Override
    public void updateCredit(UserInfo user, int credit) {
        Entity creditEntity = new Entity(CREDIT_MODEL_KIND);
        creditEntity.setProperty("userId", user.getUserId());
        creditEntity.setProperty("userEmail", user.getEmail());
        creditEntity.setProperty("updateTime", new java.util.Date());
        creditEntity.setProperty("creditValue", credit);
        datastore.put(creditEntity);
    }


    @Override
    public int currentCredit(UserInfo user) {
        Query query = new Query(CREDIT_MODEL_KIND)
            .setFilter(new FilterPredicate("userId", FilterOperator.EQUAL, user.getUserId()))
            .addSort("updateTime", SortDirection.DESCENDING);
        Entity result = datastore.prepare(query).asSingleEntity();
        int credit = 0;
        if (result != null) {
            credit = (int) result.getProperty("creditValue");
        }
        return credit;
    }
    
    public Map<Integer, String> getLeaderBoard() {
        return null;
    }
    
}
