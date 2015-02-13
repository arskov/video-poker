Video Poker Game 
=============================

Simple implementation of the Video Poker using JavaScript as a frontend and Google App Engine as a backend.
This sample describes how to implement server-side backend in Java using Google Cloud Endpoints and Javascript client library as a client of the oogle App Engine Endpoints
[Please take a looke at the demo](https://planar-night-735.appspot.com/)

Screenshots
-----------

![Alt text](/screen1.png?raw=true "Video Poker implementation in Java")
![Alt text](/screen2.png?raw=true "Video Poker implementation in Java")

How to use
----------

1. Create your own [Project on Google App Engine](https://console.developers.google.com)
2. Create client credentials as described in [docs] (https://cloud.google.com/appengine/docs/java/endpoints/consume_js#adding_authentication_support_with_oauth_20) and put them into the sources respectively.
The hint for seeking required section in the Developers Console 
![Alt text](/screen3.png?raw=true "Video Poker implementation in Java").
3. Update com.arsenyko.endpoint.Constants.WEB_CLIENT_ID and 'var TG_CLIENT_ID' in main\webapp\app\app.js to your CLIENT ID respectively
4. Deploy using 'mvn appengine:update'. Make sure if you've updated the 'application' element in the 'src/main/webapp/WEB-INF/appengine-web.xml' respectively.

Troubleshooting
---------------

If you experience troubles connected to deployment, please, get familiar with the concepts provided by [Google App Engine](https://cloud.google.com/appengine/docs/java/)

Video Poker Game on Google App Engine
-------------------------------------

[Video Poker Game on Google App Engine](https://planar-night-735.appspot.com/). Take attention to the HTTPS proto.

Acknowledgments
================

Playing Card faces by 

CSS Playing Cards
-----------------

CSS Playing Cards help you to create simple and semantic playing cards in (X)HTML.

* @author   Anika Henke <anika@selfthinker.org>
* @license  CC BY-SA [http://creativecommons.org/licenses/by-sa/3.0]
* @version  2011-06-14
* @link     http://selfthinker.github.com/CSS-Playing-Cards/
 
License
-------

The MIT License (MIT)

Copyright (c) 2014 Arseni Kavalchuk

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.