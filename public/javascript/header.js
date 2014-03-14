var headerText = "<nav class='navbar navbar-default grove-navbar' role='navigation'>" +
            "<div class='container'>" +
                "<div class='navbar-header'>" + 
                    "<a href='#' class='grove-toggle' data-toggle='collapse' data-target='.grove-nav'>   <i class='glyphicons show_lines'></i>  </a>" +
                    "<a href='index.html' class='navbar-brand'><img src='img/cetLogo.png' alt='CET'></a>" +
                "</div>" +
                "<div class='collapse navbar-collapse grove-nav'>" +
                    "<ul class='nav navbar-nav'>" +
                        "<li class='active'>" +
                            "<a href='index.html'>Home</a>" +
                        "</li>" +                      
                        "<li class='dropdown'>" +
                            "<a href='#' data-toggle='dropdown'>Pages <b class='caret'></b></a>" +
                            "<ul class='dropdown-menu'>" +
                                "<li><a href='about-us.html'>About Us</a></li>" +
                                "<li><a href='features.html'>Features Page</a></li>" +
                                "<li><a href='faq.html'>FAQ</a></li>" +
                                "<li><a href='login.html'>Login/Signup</a></li>" +
                                "<li><a href='contact.html'>Contact</a></li>" +
                            "</ul>" +
                        "</li>" +
                    "</ul>" +        
                "</div>" + ///.navbar-collapse
            "</div>" +
        "</nav>";

document.write(headerText);

