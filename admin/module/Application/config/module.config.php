<?php
/**
 * Zend Framework (http://framework.zend.com/)
 *
 * @link      http://github.com/zendframework/ZendSkeletonApplication for the canonical source repository
 * @copyright Copyright (c) 2005-2015 Zend Technologies USA Inc. (http://www.zend.com)
 * @license   http://framework.zend.com/license/new-bsd New BSD License
 */

return array(
    'router' => array(
        'routes' => array(
            'home' => array(
                'type' => 'Zend\Mvc\Router\Http\Literal',
                'options' => array(
                    'route'    => '/',
                    'defaults' => array(
                        'controller' => 'Application\Controller\Index',
                        'action'     => 'index',
                    ),
                ),
            ),
            'login' => array(
                'type' => 'Zend\Mvc\Router\Http\Literal',
                'options' => array(
                    'route'    => '/login',
                    'defaults' => array(
                        'controller' => 'Application\Controller\Index',
                        'action'     => 'login',
                    ),
                ),
            ),
            'logout' => array(
                'type' => 'Zend\Mvc\Router\Http\Literal',
                'options' => array(
                    'route'    => '/logout',
                    'defaults' => array(
                        'controller' => 'Application\Controller\Index',
                        'action'     => 'logout',
                    ),
                ),
            ),
            'users' => array(
                'type' => 'Segment',
                'options' => array(
                    'route'    => '/users[/:page]',
                    'constraints' => [
                        'id' => '[0-9]+'
                    ],
                    'defaults' => array(
                        'page' => 0,
                        'controller' => 'Application\Controller\Index',
                        'action'     => 'users',
                    ),
                ),
            ),
            'delete-user' => array(
                'type' => 'Segment',
                'options' => array(
                    'route'    => '/delete-user/:id/:page',
                    'constraints' => [
                        'id' => '[0-9a-z]+',
                        'page' => '[0-9]+'
                    ],
                    'defaults' => array(
                        'controller' => 'Application\Controller\Index',
                        'action'     => 'deleteuser',
                    ),
                ),
            ),
            'admin-user' => array(
                'type' => 'Segment',
                'options' => array(
                    'route'    => '/admin-user/:id/:page',
                    'constraints' => [
                        'id' => '[0-9a-z]+',
                        'page' => '[0-9]+'
                    ],
                    'defaults' => array(
                        'controller' => 'Application\Controller\Index',
                        'action'     => 'adminuser',
                    ),
                ),
            ),
            'update-user' => array(
                'type' => 'Segment',
                'options' => array(
                    'route'    => '/update-user/:id/:page',
                    'constraints' => [
                        'id' => '[0-9a-z]+',
                        'page' => '[0-9]+'
                    ],
                    'defaults' => array(
                        'controller' => 'Application\Controller\Index',
                        'action'     => 'updateuser',
                    ),
                ),
            ),
            'create-user' => array(
                'type' => 'Segment',
                'options' => array(
                    'route'    => '/create-user/:page',
                    'constraints' => [
                        'page' => '[0-9]+'
                    ],
                    'defaults' => array(
                        'controller' => 'Application\Controller\Index',
                        'action'     => 'createuser',
                    ),
                ),
            ),
            'feeds' => array(
                'type' => 'Zend\Mvc\Router\Http\Literal',
                'options' => array(
                    'route'    => '/feeds',
                    'defaults' => array(
                        'controller' => 'Application\Controller\Index',
                        'action'     => 'feeds',
                    ),
                ),
            ),
            'feed' => [
                'type' => 'Segment',
                'options' => [
                    'route' => '/feed[/:id[/:page]]',
                    'constraints' => [
                        'id' => '[0-9]+',
                        'page' => '[0-9]+'
                    ],
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'feed'
                    ]
                ]
            ],
            'create-feed' => [
                'type' => 'Literal',
                'options' => [
                    'route' => '/create-feed',
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'create'
                    ]
                ]
            ],
            'rename-feed' => [
                'type' => 'Literal',
                'options' => [
                    'route' => '/rename-feed',
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'renamefeed'
                    ]
                ]
            ],
            'delete-feed' => [
                'type' => 'Segment',
                'options' => [
                    'route' => '/delete-feed/:id',
                    'constraints' => [
                        'id' => '[0-9]+'
                    ],
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'deletefeed'
                    ]
                ]
            ],
            'groups' => [
                'type' => 'Literal',
                'options' => [
                    'route' => '/groups',
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'groups'
                    ]
                ]
            ],
            'create-group' => [
                'type' => 'Literal',
                'options' => [
                    'route' => '/create-group',
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'creategroup'
                    ]
                ]
            ],
            'rename-group' => [
                'type' => 'Literal',
                'options' => [
                    'route' => '/rename-group',
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'renamegroup'
                    ]
                ]
            ],
            'delete-group' => [
                'type' => 'Segment',
                'options' => [
                    'route' => '/delete-group/:id',
                    'constraints' => [
                        'id' => '[0-9]+'
                    ],
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'deletegroup'
                    ]
                ]
            ],
            'group' => [
                'type' => 'Segment',
                'options' => [
                    'route' => '/group/:id',
                    'constraints' => [
                        'id' => '[0-9]+'
                    ],
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'group'
                    ]
                ]
            ],
            'group-add-user' => [
                'type' => 'Literal',
                'options' => [
                    'route' => '/group-add-user',
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'groupadduser'
                    ]
                ]
            ],
            'group-delete-user' => [
                'type' => 'Segment',
                'options' => [
                    'route' => '/group-delete-user/:group/:user',
                    'constraints' => [
                        'group' => '[0-9]+',
                        'user' => '[0-9a-z]+',
                    ],
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'groupdeleteuser'
                    ]
                ]
            ],
            'feed-auth' => [
                'type' => 'Segment',
                'options' => [
                    'route' => '/feed-auth/:id',
                    'constraints' => [
                        'id' => '[0-9]+'
                    ],
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'feedauth'
                    ]
                ]
            ],
            'feed-auth-delete' => [
                'type' => 'Segment',
                'options' => [
                    'route' => '/feed-auth-delete/:id/:group',
                    'constraints' => [
                        'id' => '[0-9]+',
                        'group' => '[0-9]+'
                    ],
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'feedauthdelete'
                    ]
                ]
            ],
            'feed-auth-add' => [
                'type' => 'Literal',
                'options' => [
                    'route' => '/feed-auth-add',
                    'defaults' => [
                        'controller' => 'Application\Controller\Index',
                        'action' => 'feedauthadd'
                    ]
                ]
            ],
            // The following is a route to simplify getting started creating
            // new controllers and actions without needing to create a new
            // module. Simply drop new controllers in, and you can access them
            // using the path /application/:controller/:action
            'application' => array(
                'type'    => 'Literal',
                'options' => array(
                    'route'    => '/application',
                    'defaults' => array(
                        '__NAMESPACE__' => 'Application\Controller',
                        'controller'    => 'Index',
                        'action'        => 'index',
                    ),
                ),
                'may_terminate' => true,
                'child_routes' => array(
                    'default' => array(
                        'type'    => 'Segment',
                        'options' => array(
                            'route'    => '/[:controller[/:action]]',
                            'constraints' => array(
                                'controller' => '[a-zA-Z][a-zA-Z0-9_-]*',
                                'action'     => '[a-zA-Z][a-zA-Z0-9_-]*',
                            ),
                            'defaults' => array(
                            ),
                        ),
                    ),
                ),
            ),
        ),
    ),
    'service_manager' => array(
        'abstract_factories' => array(
            'Zend\Cache\Service\StorageCacheAbstractServiceFactory',
            'Zend\Log\LoggerAbstractServiceFactory',
        ),
        'factories' => array(
            'translator' => 'Zend\Mvc\Service\TranslatorServiceFactory',
        ),
    ),
    'translator' => array(
        'locale' => 'en_US',
        'translation_file_patterns' => array(
            array(
                'type'     => 'gettext',
                'base_dir' => __DIR__ . '/../language',
                'pattern'  => '%s.mo',
            ),
        ),
    ),
    'controllers' => array(
        'invokables' => array(
            'Application\Controller\Index' => 'Application\Controller\IndexController'
        ),
    ),
    'view_manager' => array(
        'display_not_found_reason' => true,
        'display_exceptions'       => true,
        'doctype'                  => 'HTML5',
        'not_found_template'       => 'error/404',
        'exception_template'       => 'error/index',
        'template_map' => array(
            'layout/layout'           => __DIR__ . '/../view/layout/layout.phtml',
            'application/index/index' => __DIR__ . '/../view/application/index/index.phtml',
            'error/404'               => __DIR__ . '/../view/error/404.phtml',
            'error/index'             => __DIR__ . '/../view/error/index.phtml',
        ),
        'template_path_stack' => array(
            __DIR__ . '/../view',
        ),
    ),
    // Placeholder for console routes
    'console' => array(
        'router' => array(
            'routes' => array(
            ),
        ),
    ),
);
