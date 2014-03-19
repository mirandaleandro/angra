/**
 * Created with IntelliJ IDEA.
 * User: leandro
 * Date: 3/14/14
 * Time: 10:17 PM
 */
$('document').ready(function(){
    $('.accordion-collapse').on('show.bs.collapse', function (n) {
        $(n.target).siblings('.accordion-toggle').toggleClass('open');
    });

    $('.accordion-collapse').on('hide.bs.collapse', function (n) {
        $(n.target).siblings('.accordion-toggle').toggleClass('open');
    });
});