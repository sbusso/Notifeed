<?php

if (!isset($GLOBALS['adapter_global']))
{
    $config = [
        'driver' => '',
        'database' => '',
        'username' => '',
        'password' => '',
        'hostname' => '',
        'options' => [
            'buffer_results' => true
        ]
    ];
    
    $GLOBALS['adapter_global'] = new Zend\Db\Adapter\Adapter($config);
}

return $GLOBALS['adapter_global'];