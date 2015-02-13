package com.arsenyko.endpoint;

import com.arsenyko.service.GameModel;
import com.arsenyko.service.PokerEngine;
import com.arsenyko.service.PokerEngineFactory;
import com.arsenyko.service.UserWrapper;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
/**
 * 
 * @author Arseny Kovalchuk
 *
 */
@Api(name = "poker", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = {
        Constants.LOCALHOST_CLIENT_ID, Constants.WEB_CLIENT_ID,
        Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID,
        Constants.API_EXPLORER_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE })
public class PokerEndpoint {

    private PokerEngine pokerEngine = PokerEngineFactory.getEngineInstance();

    @ApiMethod(name = "load", path = "poker.load")
    public GameModel load(User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("Not Authenticated");

        GameModel result = pokerEngine.load(UserWrapper.wrap(user));
        return result;
    }

    @ApiMethod(name = "deal", path = "poker.deal")
    public GameModel deal(User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("Not Authenticated");

        GameModel result = pokerEngine.deal(UserWrapper.wrap(user));
        return result;
    }

    @ApiMethod(name = "hold", path = "poker.hold", httpMethod = HttpMethod.POST)
    public void hold(@Named("card") int card, User user)
            throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("Not Authenticated");
        pokerEngine.toggleHold(UserWrapper.wrap(user), card);
    }

    @ApiMethod(name = "bet", path = "poker.bet", httpMethod = HttpMethod.POST)
    public void bet(@Named("bet") int bet, User user)
            throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("Not Authenticated");
        pokerEngine.bet(UserWrapper.wrap(user), bet);
    }

    @ApiMethod(name = "reset", httpMethod = HttpMethod.DELETE)
    public void reset(User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("Not Authenticated");
        pokerEngine.reset(UserWrapper.wrap(user));
    }

}
