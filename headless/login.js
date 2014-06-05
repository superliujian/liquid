casper.test.begin('get liquid homepage', 4, function suite(test) {
//  casper.on('resource.received', function(resource) {
//    this.echo(JSON.stringify(resource, null, 4));
//  });

  casper.start("http://localhost:8080", function() {
    test.assertTitle("Liquid", "liquid homepage title is the one expected"); 
	test.assertExists('form[action="/login"]', "login form is found");
	this.fill('form[action="/login"]', {
	  'username': '胡一兵',
      'password': '111111'
	}, true);
  });

  casper.then(function() {  
    test.assertTextExists("胡一兵", "username is found");
    test.assertTextExists("任务", "task is found");
  });

  casper.run(function() {
    test.done();
  });
});
