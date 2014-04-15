$.fn.bindTypeAhead = function()
{
    this.each(function()
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

    return this;
};


$(function()
{
   // var accordion = $('.accordion-collapse');

    function disableInputs()
    {
        if($(".readonly-state").size() > 0)
        {
            $("input").attr('readonly','readonly');
            $(".readonly-state .btn.btn-grove-one").remove();
        }
    }

    function bindDateTimePickers()
    {
        if($(".readonly-state").size() < 1)
        {
            $('.date-picker').datetimepicker({
                pickTime: false
            });

            $('.time-picker').datetimepicker({
                pickDate: false,
                pick12HourFormat: true,
                pickSeconds: false
            });
        }
    }

    function rebindFields(){

        disableInputs();
        bindDateTimePickers();
    }

    rebindFields();

    function bindBindInputsAhead (parent)
    {
        var tagInputs =  parent.find('input[data-role=tagsinput], select[multiple][data-role=tagsinput]');
        tagInputs.tagsinput();
        tagInputs.bindTypeAhead();
    }

    function mostRecentTripNumber()
    {
        return $('.trip').last().data('number');
    }

    bindBindInputsAhead($(document.body));



    $(document.body).on("click",".readonly-state input, .date-picker", function(e){
        e.stopImmediatePropagation();
    });

    $(document.body).on("click",".accordion-toggle", function(){
        $(this).siblings(".accordion-collapse").toggle("toggle");
    });


    $(document.body).on("change",".display-trigger", function()
    {
        var actionTrigger = $(this);
        var subjectToDisplayChangeStateSelector = actionTrigger.data('display-trip-form');
        var subjectToDisplayChangeState = actionTrigger.closest('.trip-content').find(subjectToDisplayChangeStateSelector);

        if( actionTrigger.is(':checked') )
            subjectToDisplayChangeState.show('slow');
        else
            subjectToDisplayChangeState.hide('slow')
    });

    $(document.body).on("click",".add-trip-action", function(e)
    {
        e.stopImmediatePropagation();

        var actionTrigger = $(this);
        var url = actionTrigger.data('url');
        var tripNumber =  mostRecentTripNumber() + 1;
        var parent = actionTrigger.closest('.trips-section');
        var trips = parent.find('.trips');

        $.post( url, {tripNumber: tripNumber},
            function( data )
        {
            //$(data).hide().appendTo(trips).show('slow');
            trips.append( data );

            var aTrip =   trips.find(".trip[data-number="+tripNumber+"]");

            aTrip.show('slow');

            bindBindInputsAhead( aTrip );
            rebindFields();
        });

    });


    $(document.body).on("click",".add-flight-action", function(e)
    {
        e.stopImmediatePropagation();

        var actionTrigger = $(this);
        var url = actionTrigger.data('url');
        var parentSelector = actionTrigger.data('parent');
        var containerSelector = actionTrigger.data('target');
        var parent = actionTrigger.closest(parentSelector);
        var tripNumber = parent.data("number") - 1;
        var planNumber = parent.closest(".plan").data("number") - 1;
        var targetContainer = parent.find(containerSelector);
        var number = targetContainer.find('.flight').last().data('number');
        var form = actionTrigger.closest('.client-request-form');

        $.post( url, {planNumber:planNumber,tripNumber:tripNumber, flightNumber: number},
            function( data )
        {
            targetContainer.append( data );

            var newElement =   parent.find(".flight[data-number="+number+"]");

            newElement.show('slow');
            rebindFields();
        });

    });

    $(document.body).on("click",".add-response-trip-action", function(e)
    {
        e.stopImmediatePropagation();

        var actionTrigger = $(this);
        var url = actionTrigger.data('url');
        var parentSelector = actionTrigger.data('parent');
        var containerSelector = actionTrigger.data('target');
        var parent = actionTrigger.closest(parentSelector);
        var planNumber = parent.closest(".plan").data("number") - 1;
        var targetContainer = parent.find(containerSelector);
        var number = targetContainer.find('.trip').last().data('number');

        $.post( url, {planNumber:planNumber, tripNumber: number},
            function( data )
        {
            targetContainer.append( data );

            var newElement =   parent.find(".trip[data-number="+number+"]");

            newElement.show('slow');
            rebindFields()
        });

    });

    $(document.body).on("click",".add-response-plan-action", function(e)
    {
        e.stopImmediatePropagation();
        var actionTrigger = $(this);
        var url = actionTrigger.data('url');
        var parentSelector = actionTrigger.data('parent');
        var containerSelector = actionTrigger.data('target');
        var parent = actionTrigger.closest(parentSelector);
        var targetContainer = parent.find(containerSelector);
        var number = targetContainer.find('.plan').last().data('number');

        $.post( url, {planNumber: number},
            function( data )
        {
            targetContainer.append( data );

            var newElement =   parent.find(".plan[data-number="+number+"]");

            newElement.show('slow');
            rebindFields()
        });

    });


    $(document.body).on("keyup",".search-input", function(){
        var input = $(this);

        var query = $.trim(input.val());

        var itemsToBeSearched = input.closest(".trips-section").find(".trips-table tbody tr");

       if(query.length < 1)
       {
          itemsToBeSearched.show();
       }
        else
       {
           itemsToBeSearched.filter( function(){

               if( $(this).text().lastIndexOf(query) == -1 )
               {
                   $(this).hide();
               }
               else
               {
                   $(this).show()
               }
           } )

       }

    });

    $(document.body).on("change","input[type=checkbox]", function(){
       $(this).attr("value",$(this).is(':checked'));
    });



});