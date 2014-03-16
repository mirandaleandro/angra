$(function(){

    var accordion = $('.accordion-collapse');
    var tagInputs =  $('input[data-role=tagsinput], select[multiple][data-role=tagsinput]');

    accordion.on('show.bs.collapse', function (n) {
        $(n.target).siblings('.accordion-toggle').toggleClass('open');
    });

    accordion.on('hide.bs.collapse', function (n) {
        $(n.target).siblings('.accordion-toggle').toggleClass('open');
    });

    tagInputs.tagsinput();

    tagInputs.each(function()
    {
        var tagInput = $(this);

        var contentSource =  tagInput.data('content');

        if(contentSource)
        {
            tagInput.tagsinput('input').typeahead(
                {
                    prefetch: contentSource
                })
                .bind('typeahead:selected', $.proxy(function (obj, datum)
                {
                    this.tagsinput('add', datum.value);
                    this.tagsinput('input').typeahead('setQuery', '');
                }, tagInput ) )
        }

    });

    $(document.body).on("change",".additional-transportation", function()
    {
        var radio = $(this);
        var rentalCompanyGroup = radio.closest('.trip').find('.form-group.preferred-rental-company');

        if( radio.hasClass('rental') )
            rentalCompanyGroup.show('slow');
        else
            rentalCompanyGroup.hide('slow')
    });

});