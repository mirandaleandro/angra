$(function(){

    $('.accordion-collapse').on('show.bs.collapse', function (n) {
        $(n.target).siblings('.accordion-toggle').toggleClass('open');
    });

    $('.accordion-collapse').on('hide.bs.collapse', function (n) {
        $(n.target).siblings('.accordion-toggle').toggleClass('open');
    });

//    $('#airlines').tagsinput({
//        typeahead: {
//            source: ['Amsterdam', 'Washington', 'Sydney', 'Beijing', 'Cairo'],
//            freeInput: true
//        }
//    });

    $('#airlines').tagsinput();
    // Adding custom typeahead support using http://twitter.github.io/typeahead.js
    $('#airlines').tagsinput('input').typeahead({
        prefetch: '/assets/json/companies.json'
    }).bind('typeahead:selected', $.proxy(function (obj, datum) {

            this.tagsinput('add', datum.value);
            this.tagsinput('input').typeahead('setQuery', '');
        }, $('#airlines')));
});