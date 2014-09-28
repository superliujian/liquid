var config = require('../config');
var user = require('../user');

user.login(config.username, config.password, function() {
  casper.thenOpen(config.baseUrl + '/order?number=0', function() {
    this.test.assertTextExists("发货订单 (订单号, 客户名称)", "find bar is found");
  });
}); 
  
casper.thenOpen(config.baseUrl + '/order/edit', function() {
  this.test.assertExists('form[action="/order/edit"]', "order form is found");
});

casper.then(function() {
  this.fill('form[action="/order/edit"]', {
    'serviceTypeId':1
  });
  this.click('button[type="submit"][name="addServiceItem"]');
});

casper.waitForSelector('input[id="serviceItems0.id"]', function() {
  this.fill('form[action="/order/edit"]', {
    'serviceTypeId':1
  });
  this.click('button[type="submit"][name="addServiceItem"]');
});

casper.waitForSelector('input[id="serviceItems1.id"]', function() {
  this.fill('form[action="/order/edit"]', {
    'serviceTypeId':1
  });
  this.click('button[type="submit"][name="addServiceItem"]');
});

casper.waitForSelector('input[id="serviceItems2.id"]', function() {
    this.fill('form[action="/order/edit"]', {
      'serviceTypeId': 2,
      'customerId': 18,
      'customerName' : '云南茶苑集团',
      'tradeType' : 1,
      'originId' : 1,
      'origination' : '昆明市',
      'destinationId' : 480,
      'destination' : '卡拉奇',
      'goodsId' : 3,
      'goodsWeight' : 8,
      'loadingType' : 0,
      'containerType' : 0,
      'railContainerSubtypeId' : 1,
      'containerQuantity' : 4,
      'planReportTime' : '2014-08-23 11:49',
      'railwayPlanTypeId' : 1,
      'railSourceId' : 1,
      'railSource' : '昆明市',
      'railDestinationId' : 480,
      'railDestination' : 'KARACHI,PAKISTAN',
      'serviceItems[0].id' : '',
      'serviceItems[0].serviceSubtypeId' : 1,
      'serviceItems[0].currency' : 'CNY',
      'serviceItems[0].quotation' : 1600,
      'serviceItems[1].id' : '',
      'serviceItems[1].serviceSubtypeId' : 2,
      'serviceItems[1].currency' : 'CNY',
      'serviceItems[1].quotation' : 4800,
      'serviceItems[2].id' : '',
      'serviceItems[2].serviceSubtypeId' : 3,
      'serviceItems[2].currency' : 'USD',
      'serviceItems[2].quotation' : 2000,
      'cnyTotal' : 6400,
      'usdTotal' : 2000
    });
    this.click('input[type="submit"][name="submit"]');
});

casper.waitForSelector('form[action="/order"]', function() {
  this.test.assertTextExists("发货订单 (订单号, 客户名称)", "find bar is found");
});

casper.run(function() {
  this.test.done();
});
