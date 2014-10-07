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

function initAcEngine_(_url, _replace) {  
    var dataset = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,  
        limit: 10,
        remote: {
            url: _url,
            replace: _replace
        }
    });
    
    dataset.initialize();
    return dataset;
}

// ac stands for auto completion
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

function initContainersAc() {
    return initAcEngine('/api/container?bicCode=%QUERY');
}

$.fn.extend({
    // hiddenId is mached by name and used to build the real input.
    autoComplete: function(dataset, hiddenId) {
        this.acWithTemplate(dataset, "name", hiddenId, '<p><strong>{{name}}</strong></p>');
    },
    acWithTemplate: function(dataset, displayKey, hiddenId, template) {
        this.typeahead(null, {
          name: this.attr('id'),
          displayKey: displayKey,
          source: dataset.ttAdapter(),
          templates: {
            suggestion: Handlebars.compile(template)
          }  
        }).on('typeahead:selected', function (obj, datum) {
            $('#' + hiddenId).val(datum.id);
        });
    }       
});
