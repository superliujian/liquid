var baseUrl = 'http://localhost:8080';
var username = '胡一兵';
var password = '111111';

var user = require('../user');

user.login(username, password, function() {
  casper.test.pass("login ok");
});

casper.run(function() {
  this.test.done();
});
