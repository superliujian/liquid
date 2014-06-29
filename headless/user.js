var baseUrl = 'http://localhost:8080';

var require = patchRequire(require);

exports.login = function(username, password, test) {
  casper.start();

  casper.open(baseUrl);

  casper.then(function() {
    this.fill('form[action="/login"]', {
      'username': username,
      'password': password
    }, true);
  });

  casper.waitForSelector('i[class="glyphicon glyphicon-user"]', function() {
    this.echo(username + ' login to liquid');
    test.call(); 
  });
};

