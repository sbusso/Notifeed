<?php

// Initialize autoloading
chdir(dirname(__DIR__));
if (file_exists('vendor/autoload.php'))
    require 'vendor/autoload.php';
else
    die ('Composer hasn\'t been run yet. Please run <i>composer install</i> on your server\'s main directory. You may need to install composer beforehand.');

require 'app/User.php';
$url = "http://" . $_SERVER['HTTP_HOST'] . str_replace("?{$_SERVER['QUERY_STRING']}", '', $_SERVER['REQUEST_URI']);

if (isset($_GET['wsdl']))
{
    header('Content-type: text/xml; charset=utf-8');
    $server = new Zend\Soap\AutoDiscover();
    $server->setClass('User')
           ->setUri($url)
           ->setServiceName('Notifeed server');
    $wsdl = $server->generate();
    echo $wsdl->toXML();
} else{
    $server = new Zend\Soap\Server(null, ['uri' => $url]);
    $server->setClass('User');
    $server->handle();
}