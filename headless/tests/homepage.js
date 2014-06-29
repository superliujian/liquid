var config = require('../config');

casper.test.begin('get liquid homepage', 2, function suite(test) {
  casper.start(config.baseUrl, function() {
    test.assertTitle("Liquid", "liquid homepage title is the one expected"); 
	test.assertExists('form[action="/login"]', "main form is found");
  });

  casper.run(function() {
    test.done();
  });
});
