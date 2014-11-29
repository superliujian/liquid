function initAcEngine(r) {  
    var dataset = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,  
        limit: 10,
        remote: r
    });
    
    dataset.initialize();
    return dataset;
}

// ac stands for auto completion
function initCustomerAc() {
    return initAcEngine({url: '/api/customer?name=%QUERY'});
}

function initSpAc() {
    return initAcEngine({url: '/api/sp?name=%QUERY'});
}

function initLocationsAc(type) {
    return initAcEngine({ 
        url: '/api/location',
        replace: function(url, query) {
            return url + '?type=' + type + '&name=' + encodeURIComponent(query);
        }        
    });
}

function initAllLocationsAc() {
    return initAcEngine({url: '/api/location?name=%QUERY'});
}

function initContainersAc() {
    return initAcEngine({url: '/api/container?bicCode=%QUERY'});
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
    },
    dtPicker: function() {
        this.datetimepicker({
            language: 'zh-cn'
        });    
    }      
});
