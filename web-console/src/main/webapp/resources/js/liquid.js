function initAcEngine(uri) {  
    var dataset = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,  
        limit: 10,
        remote: uri
    });
    
    dataset.initialize();
    return dataset;
}

function initCustomerAc() {
    return initAcEngine('/api/customer?name=%QUERY');
}

function initCitiesAc() {
    return initAcEngine('/api/location?type=0&name=%QUERY');
}

function initStationsAc() {
    return initAcEngine('/api/location?type=1&name=%QUERY');
}

function initPortsAc() {
    return initAcEngine('/api/location?type=2&name=%QUERY');
}

function initPortsAc() {
    return initAcEngine('/api/location?type=2&name=%QUERY');
}

$.fn.extend({
    autoComplete: function(dataset, hiddenId) {
        this.acWithTemplate(dataset, hiddenId, '<p><strong>{{name}}</strong></p>');
    },
    acWithTemplate: function(dataset, hiddenId, template) {
        this.typeahead(null, {
          name: this.attr('id'),
          displayKey: 'name',
          source: dataset.ttAdapter(),
          templates: {
            suggestion: Handlebars.compile(template)
          }  
        }).on('typeahead:selected', function (obj, datum) {
            $('#' + hiddenId).val(datum.id);
        });
    }       
});
