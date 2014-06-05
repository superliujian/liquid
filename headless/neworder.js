casper.test.begin('get liquid homepage', 7, function suite(test) {
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

  casper.then(function() {
    this.fill('form[action="/order/edit"]', {
      'serviceTypeId':1
    });
    this.click('button[type="submit"][name="addServiceItem"]');
  });

  casper.then(function() {
    this.fill('form[action="/order/edit"]', {
      'serviceTypeId':1
    });
    this.click('button[type="submit"][name="addServiceItem"]');
  });

  casper.then(function() {
    this.fill('form[action="/order/edit"]', {
      'serviceTypeId':1
    });
    this.click('button[type="submit"][name="addServiceItem"]');
  });

  casper.then(function() {
      this.fill('form[action="/order/edit"]', {
        'serviceTypeId': 2,
        'customerId': 18,
        'customerName' : '云南茶苑集团',
        'tradeType' : 1,
        'originId' : 1, 
        'destinationId' : 7,
        'goodsId' : 3,
        'goodsWeight' : 8,
        'loadingType' : 0,
        'containerType' : 0,
  	    'railContainerSubtypeId' : 1,
  	    'containerQuantity' : 4,
  	    'serviceItems[0].id' : '',
        'serviceItems[0].serviceSubtypeId' : 1,
        'serviceItems[0].currency' : 'CNY',
        'serviceItems[0].quotation' : 1600,
        'serviceItems[1].id' : '',
        'serviceItems[1].serviceSubtypeId' : 2,
        'serviceItems[1].currency' : 'CNY',
        'serviceItems[1].quotation' : 4800,
//        'serviceItems[2].id' : '',
//        'serviceItems[2].serviceSubtypeId' : 3,
//        'serviceItems[2].currency' : 'USD',
//        'serviceItems[2].quotation' : 2000,
        'cnyTotal' : 6400,
        'usdTotal' : 1 
      });
      this.click('input[type="submit"][name="save"]');
  });

  casper.then(function() {
    this.wait(1000, function() {
      this.echo("I've waited for a second.");
      test.assertTextExists("发货订单 (订单号, 客户名称)", "find bar is found");
    });
  });

  casper.run(function() {
    test.done();
  });
});
