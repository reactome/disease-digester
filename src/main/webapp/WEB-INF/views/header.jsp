
<!DOCTYPE html>

<html xmlns="//www.w3.org/1999/xhtml" xml:lang="en-gb" lang="en-gb" dir="ltr" >

<head>
	
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="pathway,reactions,graph,bioinformatics" />
	<meta name="robots" content="index, follow" />
	<meta name="description" content="Reactome is pathway database which provides intuitive bioinformatics tools for the visualisation, interpretation and analysis of pathway knowledge." />
	<meta name="generator" content="Joomla! - Open Source Content Management" />
	<title>Reactome | DisGeNET overlay of gene-disease associations</title>
	<link href="/template-swagger?format=feed&amp;type=rss" rel="alternate" type="application/rss+xml" title="RSS 2.0" />
	<link href="/template-swagger?format=feed&amp;type=atom" rel="alternate" type="application/atom+xml" title="Atom 1.0" />
	<link href="/templates/favourite/favicon.ico" rel="shortcut icon" type="image/vnd.microsoft.icon" />
	<link href="/media/jui/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="/media/jui/css/bootstrap-responsive.css" rel="stylesheet" type="text/css" />
	<link href="/templates/favourite/bootstrap/favth-bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="/modules/mod_favsocial/theme/css/favsocial.css" rel="stylesheet" type="text/css" />
	<link href="//maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	<script type="application/json" class="joomla-script-options new">{"csrf.token":"e5362203f78e64dea578901a3acb9c2c","system.paths":{"root":"","base":""}}</script>
	<script src="/media/jui/js/jquery.min.js?a8982b90a051b3d70d506a1e07e938cf" type="text/javascript"></script>
	<script src="/media/jui/js/jquery-noconflict.js?a8982b90a051b3d70d506a1e07e938cf" type="text/javascript"></script>
	<script src="/media/jui/js/jquery-migrate.min.js?a8982b90a051b3d70d506a1e07e938cf" type="text/javascript"></script>
	<script src="/media/system/js/caption.js?a8982b90a051b3d70d506a1e07e938cf" type="text/javascript"></script>
	<script src="/media/system/js/core.js?a8982b90a051b3d70d506a1e07e938cf" type="text/javascript"></script>
	<script src="/media/jui/js/bootstrap.min.js?a8982b90a051b3d70d506a1e07e938cf" type="text/javascript"></script>
	<script src="/templates/favourite/bootstrap/favth-bootstrap.js" type="text/javascript"></script>
	<script src="/media/jui/js/jquery.autocomplete.min.js?a8982b90a051b3d70d506a1e07e938cf" type="text/javascript"></script>
	<script type="text/javascript">
jQuery(window).on('load',  function() {
				new JCaption('img.caption');
			});
	</script>


	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

	<!-- STYLESHEETS -->
    <!-- Custom: type is not needed, remove it -->
    <!-- icons -->
  	<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" />
    <!-- admin -->
    <link rel="stylesheet" href="/templates/favourite/admin/admin.css" />
    <!-- cms -->
    <link rel="stylesheet" href="/templates/favourite/css/cms.css" />
    <!-- theme -->
    <link rel="stylesheet" href="/templates/favourite/css/theme.css" />
    <!-- style -->
    <link rel="stylesheet" href="/templates/favourite/css/style.css" />
    <!-- styles -->
    <link rel="stylesheet" href="/templates/favourite/css/styles/style2.css" />
    <!-- Custom: Our custom css -->
    <link rel="stylesheet" href="/templates/favourite/css/custom.css?v=1.1.1" />
    <!-- Custom: Stylying the autocomplete div-->
    <link rel="stylesheet" href="/templates/favourite/css/autocomplete/autocomplete.css" />
    <!-- Custom: rglyph fonts -->
    <link rel="stylesheet" href="/templates/favourite/rglyph/rglyph.css" />

    <!-- GOOGLE FONT -->
    <!-- navigation -->
    <link href='//fonts.googleapis.com/css?family=Open+Sans:400' rel='stylesheet' />
    <!-- titles -->
    <link href='//fonts.googleapis.com/css?family=Open+Sans:300' rel='stylesheet' />
    <!-- text logo -->
    <link href='//fonts.googleapis.com/css?family=Source+Sans+Pro:700' rel='stylesheet' />
    <!-- default -->
    <link href="//fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" />
    <!-- custom: icon for phone app -->
    <link rel="apple-touch-icon" sizes="128x128" href="/templates/favourite/images/logo/icon.png">

  <!-- PARAMETERS -->
  
<!-- Custom: remove type -->
<style>

  @media (min-width: 1200px) {
    .favth-container,
    #fav-headerwrap.fav-fixed .favth-container-block {
      width: 1190px;
    }
  }
  .favnav ul.nav > li > a,
  .favnav ul.nav > li > .nav-header,
  .favnav ul.nav ul.nav-child a,
  .favnav ul.nav ul.nav-child .nav-header,
  ul.menufavth-horizontal li a,
  ul.menufavth-horizontal li .nav-header {
    text-transform: uppercase;
  }
  .favnav ul.nav > li > a,
  .favnav ul.nav > li > .nav-header,
  .favnav ul.nav ul.nav-child a,
  .favnav ul.nav ul.nav-child .nav-header,
  ul.menufavth-horizontal li a,
  ul.menufavth-horizontal li .nav-header {
    font-family: 'Open Sans', sans-serif;
  }
  .favnav ul.nav > li > a,
  .favnav ul.nav > li > .nav-header,
  .favnav ul.nav ul.nav-child a,
  .favnav ul.nav ul.nav-child .nav-header,
  ul.menufavth-horizontal li a,
  ul.menufavth-horizontal li .nav-header {
    font-weight: 400;
  }
  .favnav ul.nav > li > a,
  .favnav ul.nav > li > .nav-header,
  .favnav ul.nav ul.nav-child a,
  .favnav ul.nav ul.nav-child .nav-header,
  ul.menufavth-horizontal li a,
  ul.menufavth-horizontal li .nav-header {
    font-style: normal;
  }
  .fav-container h3:first-of-type,
  .fav-container .page-header h2,
  .fav-container h2.item-title,
  .fav-container .hikashop_product_page h1 {
    text-align: left;
  }
  .fav-container h3:first-of-type,
  .fav-container .page-header h2,
  .fav-container h2.item-title,
  .fav-container .hikashop_product_page h1 {
    text-transform: uppercase;
  }
  .fav-container h1,
  .fav-container h2,
  .fav-container h3,
  .fav-container h4,
  .fav-container h5,
  .fav-container h6,
  .fav-container legend {
    font-family: 'Open Sans', sans-serif;
  }
  .fav-container h1,
  .fav-container h2,
  .fav-container h3,
  .fav-container h4,
  .fav-container h5,
  .fav-container h6,
  .fav-container legend {
    font-weight: 300;
  }
  .fav-container h1,
  .fav-container h2,
  .fav-container h3,
  .fav-container h4,
  .fav-container h5,
  .fav-container h6,
  .fav-container legend {
    font-style: normal;
  }
  #fav-offlinewrap {
    background-repeat: no-repeat; background-attachment: fixed; -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover;;
  }
  body {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-noticewrap.fav-module-block-color {
    background-color: #FCF8E3;
  }
  #fav-noticewrap p {
    color: #8A6D3B;
  }
  #fav-topbarwrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-topbarwrap.fav-module-block-color {
    background-color: #F2DEDE;
  }
  #fav-topbarwrap p {
    color: #A94442;
  }
  #fav-slidewrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-slidewrap.fav-module-block-color {
    background-color: #F0F0F0;
  }
  #fav-introwrap.fav-module-block-clear {
    background-repeat: no-repeat; background-attachment: fixed; -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover;;
  }
  #fav-breadcrumbswrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-leadwrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-promowrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-primewrap.fav-module-block-clear {
    background-repeat: no-repeat; background-attachment: fixed; -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover;;
  }
  #fav-primewrap.fav-module-block-color {
    background-color: #FFFFFF;
  }
  #fav-showcasewrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-showcasewrap.fav-module-block-color {
    background-color: #111111;
  }
  #fav-featurewrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-focuswrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-portfoliowrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-screenwrap.fav-module-block-clear {
    background-repeat: no-repeat; background-attachment: fixed; -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover;;
  }
  #fav-screenwrap.fav-module-block-color {
    background-color: #F0F0F0;
  }
  #fav-screenwrap h3:first-of-type {
    color: #444444;
  }
  #fav-topwrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-topwrap h3:first-of-type {
    color: #444444;
  }
  #fav-maintopwrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-mainbottomwrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-bottomwrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-notewrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-basewrap.fav-module-block-clear {
    background-repeat: no-repeat; background-attachment: fixed; -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover;;
  }
  #fav-blockwrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-userwrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-userwrap.fav-module-block-color {
    background-color: #F0F0F0;
  }
  #fav-footerwrap.fav-module-block-clear {
    background-repeat: repeat; background-attachment: initial; -webkit-background-size: auto; -moz-background-size: auto; -o-background-size: auto; background-size: auto;;
  }
  #fav-footerwrap.fav-module-block-color {
    background-color: #58C3E5;
  }
  #fav-footerwrap p {
    color: #FFFFFF;
  }
  #fav-footerwrap a {
    color: #FFFFFF;
  }
  .fav-container a.text-logo,
  .fav-container a.text-logo:hover,
  .fav-container a.text-logo:focus {
    color: #58C3E5;
  }
  .fav-container a.text-logo {
    font-size: 42px;
  }
  .fav-container a.text-logo,
  #fav-logo h1 {
    font-family: 'Source Sans Pro', sans-serif;
  }
  .fav-container a.text-logo,
  #fav-logo h1 {
    font-weight: 700;
  }
  .fav-container a.text-logo,
  #fav-logo h1 {
    font-style: normal;
  }
  .fav-container a.text-logo {
    margin: 6px 0 0 0;
  }
  .slogan {
    line-height: 14px;
  }
  .retina-logo {
    height: 52px;
  }
  .retina-logo {
    width: 188px;
  }
  .retina-logo {
    padding: 0px;
  }
  .retina-logo {
    margin: 0px;
  }
  @media (max-width: 480px) {
    p {
      font-size: 12px;
    }
  }

</style>

  <!-- GOOGLE ANALYTICS TRACKING CODE -->
  
  <!-- Custom: Global Site Tag (gtag.js) - Google Analytics -->
  <script async src="https://www.googletagmanager.com/gtag/js?id=UA-42985898-2"></script>
  <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments)};
      gtag('js', new Date());
      gtag('config', 'UA-42985898-2');
  </script>

  <!-- FAVTH SCRIPTS -->
  <script src="/templates/favourite/js/favth-scripts.js"></script>

  <!-- Google Webmaster Tools -->
  <meta name="google-site-verification" content="RTiL6uaaS_DH5XmjYzK76jixDNHfH2KicYr2jT2y1Cw" />
  <script type="application/ld+json">
  {
    "@context": "http://schema.org",
    "@type": "WebSite",
    "url": "https://reactome.org/",
    "potentialAction": {
      "@type": "SearchAction",
      "target": "https://reactome.org/content/query?q={term}",
      "query-input": "required name=term"
    }
  }
  </script>

  <script type="application/ld+json">
  {
    "@context": "http://schema.org",
    "@type": "Organization",
    "url": "https://reactome.org",
    "logo": "https://reactome.org/templates/favourite/images/logo/logo.png",
    "email": "help@reactome.org"
  }
  </script>
<link rel="stylesheet" href="/overlay/resources/css/overlay.css?v=20191121" type="text/css" />
</head>

<!-- Custom: added ontouchstart="" -->
<body ontouchstart="">

  <div id="fav-containerwrap" class="favth-clearfix">
    <div class="fav-transparent">

  	  <!-- NAVBAR -->
      <div id="fav-navbarwrap" class="favth-visible-xs">
    		<div class="favth-navbar favth-navbar-default">
          <div id="fav-navbar" class="favth-container">

    				<div class="favth-navbar-header">
              <div id="fav-logomobile" class="favth-clearfix">
                                  <h1>
                    <a class="default-logo" href="/">
                      <img src="/templates/favourite/images/logo/logo.png" style="border:0;" alt="Reactome Pathway Database" />
                    </a>
                  </h1>
                                                                                              </div>

              
              <div id="fav-navbar-btn" class="favth-clearfix">
                <button type="button" class="favth-navbar-toggle favth-collapsed" data-toggle="favth-collapse" data-target=".favth-collapse" aria-expanded="false">

                  <span class="favth-sr-only">Toggle navigation</span>
                  <span class="favth-icon-bar"></span>
                  <span class="favth-icon-bar"></span>
                  <span class="favth-icon-bar"></span>
                </button>
              </div>
            </div>

  					<div class="favth-collapse favth-navbar-collapse">
  						  							<div id="fav-navbar-collapse">
                    								<div class="moduletable" ><ul class="nav menu mod-list">
<li class="item-234 deeper parent"><span class="nav-header fa-info-circle">About</span>
<ul class="nav-child unstyled small"><li class="item-235"><a href="/what-is-reactome" >What is Reactome ?</a></li><li class="item-236"><a href="/about/news" >News</a></li><li class="item-237"><a href="/about/team" >Team</a></li><li class="item-251"><a href="/sab" >Scientific Advisory Board</a></li><li class="item-362"><a href="/about/editorial-calendar" >Editorial Calendar</a></li><li class="item-360"><a href="/about/statistics" >Statistics</a></li><li class="item-397"><a href="/about/logo" >Our Logo</a></li><li class="item-253"><a href="/license" >License Agreement</a></li><li class="item-254"><a href="/about/disclaimer" >Disclaimer</a></li></ul></li><li class="item-356 deeper parent"><span class="nav-header fa-pencil-square-o">Content</span>
<ul class="nav-child unstyled small"><li class="item-358"><a href="/cgi-bin/toc?DB=current" >Table of Contents</a></li><li class="item-359"><a href="/cgi-bin/doi_toc?DB=current" >DOIs</a></li><li class="item-357"><a href="/content/schema" >Data Schema</a></li><li class="item-361"><a href="/orcid" >ORCID Integration Project</a></li><li class="item-499"><a href="/overlay/disgenet" >DisGeNET</a></li></ul></li><li class="item-152 deeper parent"><a href="/documentation" class="fa-graduation-cap">Docs</a><ul class="nav-child unstyled small"><li class="item-303 deeper parent"><a href="/userguide" >Userguide</a><ul class="nav-child unstyled small"><li class="item-378"><a href="/userguide/pathway-browser" >Pathway Browser</a></li><li class="item-380"><a href="/userguide/searching" >How do I search ?</a></li><li class="item-379"><a href="/userguide/details-panel" >Details Panel</a></li><li class="item-381"><a href="/userguide/analysis" >Analysis Tools</a></li><li class="item-382"><a href="/userguide/diseases" >Diseases</a></li><li class="item-479"><a href="/userguide/reactome-fiviz" >ReactomeFIViz</a></li></ul></li><li class="item-268 deeper parent"><a href="/dev" >Developer's Zone</a><ul class="nav-child unstyled small"><li class="item-290"><a href="/dev/graph-database" >Graph Database</a></li><li class="item-292"><a href="/dev/analysis" >Analysis Service </a></li><li class="item-293"><a href="/dev/content-service" >Content Service</a></li><li class="item-294"><a href="/dev/pathways-overview" >Pathways Overview</a></li><li class="item-295"><a href="/dev/diagram" >Pathway Diagrams</a></li></ul></li><li class="item-302 deeper parent"><a href="/icon-info" >Icon Info</a><ul class="nav-child unstyled small"><li class="item-471"><a href="/icon-info/ehld-specs-guideline" >EHLD Specs &amp; Guidelines</a></li><li class="item-472"><a href="/icon-info/icons-guidelines" >Icon Library Guidelines</a></li></ul></li><li class="item-269"><a href="/documentation/data-model" >Data Model</a></li><li class="item-270"><a href="/documentation/inferred-events" >Computationally inferred events</a></li><li class="item-271"><a href="/linking-to-us" >Linking to Us</a></li><li class="item-272"><a href="/cite" >Citing us</a></li></ul></li><li class="item-238 deeper parent"><span class="nav-header fa-cogs">Tools</span>
<ul class="nav-child unstyled small"><li class="item-239"><a href="/PathwayBrowser" >Pathway Browser</a></li><li class="item-240"><a href="/PathwayBrowser/#TOOL=AT" >Analyze Data</a></li><li class="item-245"><a href="/PathwayBrowser/#TOOL=AT" >Species Comparison</a></li><li class="item-248"><a href="/AnalysisService" >Analysis Service</a></li><li class="item-249"><a href="/ContentService" >Content Service</a></li><li class="item-246"><a href="/tools/reactome-fiviz" >ReactomeFIViz</a></li><li class="item-247"><a href="/content/advanced" >Advanced Data Search</a></li><li class="item-301"><a href="/tools/site-map" >Site map</a></li></ul></li><li class="item-241 deeper parent"><span class="nav-header fa-users">Community</span>
<ul class="nav-child unstyled small"><li class="item-266"><a href="/icon-lib" title="Community library of icons that are used in the Reactome Enhanced High Level Diagrams (EHLD). We would like to develop the EHLD library further as a community resource. If you use library elements, and design similar elements that are still missing, we would be very happy to incorporate them into the library, naming you as the author" class="icons-library-mi">Icon Library</a></li><li class="item-259"><a href="/community/outreach" >Outreach</a></li><li class="item-260"><a href="/community/events" >Events</a></li><li class="item-261"><a href="/community/training" >Training</a></li><li class="item-262"><a href="/community/publications" >Publications</a></li><li class="item-263"><a href="/community/partners" >Partners</a></li><li class="item-264"><a href="/community/citation-list" >Papers Citing Us</a></li><li class="item-265"><a href="/community/resources" >Resources Guide</a></li><li class="item-493"><a href="/community/collaboration" >Collaboration</a></li></ul></li><li class="item-242"><a href="/download-data" class="fa-download">Download</a></li></ul>
</div>
  							</div>
  						  					</div>

    			</div>
    	  </div>
      </div>

  		<div id="fav-container" class="fav-container">

  			<!-- NOTICE -->
            <!-- Custom: do not show notice in production $app->get('show_notice_mod') != 0) -->
  			
  				<div id="fav-noticewrap" class="favth-alert favth-alert-warning favth-alert-dismissible fav-module-block-color" role="alert" aria-label="Close">
            <div class="favth-container">

                    					<div class="favth-row">
    						<div id="fav-notice" class=" favth-clearfix">

                  <div class="favth-col-lg-12 favth-col-md-12 favth-col-sm-12 favth-col-xs-12">

                    <div class="moduletable" >

<div class="custom"  >
	<div class="hidden-phone">
<p class="rlp-noticemsg" style="margin-top: 5px;"><i class="fa fa-exclamation-triangle fa-1"></i><span style="font-size: 8pt;">THIS SITE IS USED FOR SOFTWARE DEVELOPMENT AND TESTING&nbsp;</span><br /> <span style="font-size: 8pt;">IT IS NOT STABLE, IS LINKED TO AN INCOMPLETE DATA SET, AND IS NOT MONITORED FOR PERFORMANCE. WE STRONGLY RECOMMEND THE USE OF OUR&nbsp;<a href="//reactome.org/">PUBLIC SITE</a></span></p>
</div>
<div class="hidden-tablet hidden-desktop">
<p class="rlp-noticemsg"><i class="fa fa-exclamation-triangle fa-1"></i><span style="font-size: 8pt;">THIS SITE IS USED FOR SOFTWARE DEVELOPMENT AND TESTING&nbsp;</span><span style="font-size: 8pt;">WE STRONGLY RECOMMEND THE USE OF OUR&nbsp;<a href="//reactome.org/">PUBLIC SITE</a></span></p>
</div></div>
</div>

                  </div>

                </div>
    					</div>

            </div>
    			</div>

  			
        <!-- TOPBAR -->
        
        <!-- HEADER -->
        <div id="fav-headerwrap">
          <div class="favth-container">
            <div class="favth-row">

                <div id="fav-header" class="favth-clearfix">

                  <div id="fav-logo" class="favth-col-lg-3 favth-col-md-3 favth-col-sm-12 favth-hidden-xs">
                                          <h1>
                        <a class="default-logo" href="/">
                          <img src="/templates/favourite/images/logo/logo.png" style="border:0;" alt="Reactome Pathway Database" />
                        </a>
                      </h1>
                                                                                                                      </div>

                                  <div id="fav-nav" class="favth-col-lg-9 favth-col-md-9 favth-col-sm-12 favth-hidden-xs">
                    <div class="favnav">
                      <div class="favth-clearfix">
                        <div class="moduletable" ><ul class="nav menu mod-list">
<li class="item-234 deeper parent"><span class="nav-header fa-info-circle">About</span>
<ul class="nav-child unstyled small"><li class="item-235"><a href="/what-is-reactome" >What is Reactome ?</a></li><li class="item-236"><a href="/about/news" >News</a></li><li class="item-237"><a href="/about/team" >Team</a></li><li class="item-251"><a href="/sab" >Scientific Advisory Board</a></li><li class="item-362"><a href="/about/editorial-calendar" >Editorial Calendar</a></li><li class="item-360"><a href="/about/statistics" >Statistics</a></li><li class="item-397"><a href="/about/logo" >Our Logo</a></li><li class="item-253"><a href="/license" >License Agreement</a></li><li class="item-254"><a href="/about/disclaimer" >Disclaimer</a></li></ul></li><li class="item-356 deeper parent"><span class="nav-header fa-pencil-square-o">Content</span>
<ul class="nav-child unstyled small"><li class="item-358"><a href="/cgi-bin/toc?DB=current" >Table of Contents</a></li><li class="item-359"><a href="/cgi-bin/doi_toc?DB=current" >DOIs</a></li><li class="item-357"><a href="/content/schema" >Data Schema</a></li><li class="item-361"><a href="/orcid" >ORCID Integration Project</a></li><li class="item-499"><a href="/overlay/disgenet" >DisGeNET</a></li></ul></li><li class="item-152 deeper parent"><a href="/documentation" class="fa-graduation-cap">Docs</a><ul class="nav-child unstyled small"><li class="item-303 deeper parent"><a href="/userguide" >Userguide</a><ul class="nav-child unstyled small"><li class="item-378"><a href="/userguide/pathway-browser" >Pathway Browser</a></li><li class="item-380"><a href="/userguide/searching" >How do I search ?</a></li><li class="item-379"><a href="/userguide/details-panel" >Details Panel</a></li><li class="item-381"><a href="/userguide/analysis" >Analysis Tools</a></li><li class="item-382"><a href="/userguide/diseases" >Diseases</a></li><li class="item-479"><a href="/userguide/reactome-fiviz" >ReactomeFIViz</a></li></ul></li><li class="item-268 deeper parent"><a href="/dev" >Developer's Zone</a><ul class="nav-child unstyled small"><li class="item-290"><a href="/dev/graph-database" >Graph Database</a></li><li class="item-292"><a href="/dev/analysis" >Analysis Service </a></li><li class="item-293"><a href="/dev/content-service" >Content Service</a></li><li class="item-294"><a href="/dev/pathways-overview" >Pathways Overview</a></li><li class="item-295"><a href="/dev/diagram" >Pathway Diagrams</a></li></ul></li><li class="item-302 deeper parent"><a href="/icon-info" >Icon Info</a><ul class="nav-child unstyled small"><li class="item-471"><a href="/icon-info/ehld-specs-guideline" >EHLD Specs &amp; Guidelines</a></li><li class="item-472"><a href="/icon-info/icons-guidelines" >Icon Library Guidelines</a></li></ul></li><li class="item-269"><a href="/documentation/data-model" >Data Model</a></li><li class="item-270"><a href="/documentation/inferred-events" >Computationally inferred events</a></li><li class="item-271"><a href="/linking-to-us" >Linking to Us</a></li><li class="item-272"><a href="/cite" >Citing us</a></li></ul></li><li class="item-238 deeper parent"><span class="nav-header fa-cogs">Tools</span>
<ul class="nav-child unstyled small"><li class="item-239"><a href="/PathwayBrowser" >Pathway Browser</a></li><li class="item-240"><a href="/PathwayBrowser/#TOOL=AT" >Analyze Data</a></li><li class="item-245"><a href="/PathwayBrowser/#TOOL=AT" >Species Comparison</a></li><li class="item-248"><a href="/AnalysisService" >Analysis Service</a></li><li class="item-249"><a href="/ContentService" >Content Service</a></li><li class="item-246"><a href="/tools/reactome-fiviz" >ReactomeFIViz</a></li><li class="item-247"><a href="/content/advanced" >Advanced Data Search</a></li><li class="item-301"><a href="/tools/site-map" >Site map</a></li></ul></li><li class="item-241 deeper parent"><span class="nav-header fa-users">Community</span>
<ul class="nav-child unstyled small"><li class="item-266"><a href="/icon-lib" title="Community library of icons that are used in the Reactome Enhanced High Level Diagrams (EHLD). We would like to develop the EHLD library further as a community resource. If you use library elements, and design similar elements that are still missing, we would be very happy to incorporate them into the library, naming you as the author" class="icons-library-mi">Icon Library</a></li><li class="item-259"><a href="/community/outreach" >Outreach</a></li><li class="item-260"><a href="/community/events" >Events</a></li><li class="item-261"><a href="/community/training" >Training</a></li><li class="item-262"><a href="/community/publications" >Publications</a></li><li class="item-263"><a href="/community/partners" >Partners</a></li><li class="item-264"><a href="/community/citation-list" >Papers Citing Us</a></li><li class="item-265"><a href="/community/resources" >Resources Guide</a></li><li class="item-493"><a href="/community/collaboration" >Collaboration</a></li></ul></li><li class="item-242"><a href="/download-data" class="fa-download">Download</a></li></ul>
</div>
                      </div>
                    </div>
                  </div>
                
                </div>

            </div>
          </div>
        </div>

        <!-- SLIDE -->
                  <div id="fav-slidewrap" class="fav-module-block-light">
            <div class="fav-transparent">
              <div class="favth-container">
                <div class="favth-row">

                  <div id="fav-slide" class=" favth-clearfix">
                    <div class="favth-col-lg-12 favth-col-md-12 favth-col-sm-12 favth-col-xs-12">
                      <div class="moduletable text-center alt-search" >

<div class="custom text-center alt-search"  >
	<div class="search"><form action="/content/query" method="get" class="clean-form form-inline"><label for="local-searchbox" class="element-invisible">Search ...</label> <input maxlength="200" name="q" size="10" type="search" id="local-searchbox" class="inputbox search-query alt-searchbox" autofocus="" placeholder="e.g. O95631, NTN1, signaling by EGFR, glucose" /> <input name="species" type="hidden" value="Homo sapiens" /> <input name="species" type="hidden" value="Entries without species" /> <input name="cluster" type="hidden" value="true" /> <button class="button btn btn-primary btn-info">Go!</button></form></div></div>
</div>
                    </div>
                  </div>

                </div>
              </div>
            </div>
          </div>
        
  			<!-- INTRO -->
        
        
        <!-- LEAD -->
        
          <div id="fav-leadwrap" class="fav-module-block-light">
            <div class="fav-transparent">
              <div class="favth-container">
                <div class="favth-row">

                  <div id="fav-lead" class=" favth-clearfix">

                                          
                          <div id="fav-lead1" class="favth-col-lg-12 favth-col-md-12 favth-col-sm-12 favth-col-xs-12">

                            <div class="moduletable" >

<div class="custom"  >
	