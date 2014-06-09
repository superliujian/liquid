var require = patchRequire(require);

exports.answer = function() {
  casper.echo('hi');
};

casper.start();

answer();

casper.run();
