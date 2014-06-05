casper.test.begin('get liquid homepage', 6, function suite(test) {
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

  casper.thenOpen('http://localhost:8080/order?number=0', function() {
    test.assertTextExists("发货订单 (订单号, 客户名称)", "find bar is found");
  });

  casper.thenOpen('http://localhost:8080/order/edit', function() {
    test.assertExists('form[action="/order/edit"]', "order form is found");
  });

  casper.run(function() {
    test.done();
  });
});
