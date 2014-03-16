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

    $(document.body).on("change",".display-trigger", function()
    {
        var actionTrigger = $(this);
        var subjectToDisplayChangeStateSelector = actionTrigger.data('display-trip-form');
        var subjectToDisplayChangeState = actionTrigger.closest('.trip').find(subjectToDisplayChangeStateSelector);

        if( actionTrigger.is(':checked') )
            subjectToDisplayChangeState.show('slow');
        else
            subjectToDisplayChangeState.hide('slow')
    });


});