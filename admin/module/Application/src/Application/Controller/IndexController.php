<?php
/**
 * Zend Framework (http://framework.zend.com/)
 *
 * @link      http://github.com/zendframework/ZendSkeletonApplication for the canonical source repository
 * @copyright Copyright (c) 2005-2015 Zend Technologies USA Inc. (http://www.zend.com)
 * @license   http://framework.zend.com/license/new-bsd New BSD License
 */

namespace Application\Controller;

use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;

class IndexController extends AbstractActionController
{
    public function indexAction()
    {
        $container = new \Zend\Session\Container('datas');
        $datas = [
            'login' => isset($container->login) ? $container->login : '',
            'password' => isset($container->password) ? $container->password : '',
            'server' => isset($container->server) ? $container->server : '',
            'error' => isset($container->error) ? $container->error : ''
        ];
        
        return new ViewModel($datas);
    }
    
    public function loginAction()
    {
        $container = new \Zend\Session\Container('datas');
        $container->login = $this->params()->fromPost('login', '');
        $container->password = $this->params()->fromPost('password', '');
        $container->server = $this->params()->fromPost('server', '');
        
        $result = $this->_soap()->register_account($container->login, $container->password);
        if ($result === 'Invalid username or password')
        {
            $container->error = 'Identifiant ou mot de passe incorrect';
            return $this->redirect()->toRoute('home');
        } else {
            if ($this->_soap()->is_admin_key($result))
            {
                $container->acc_key = $result;
                return $this->redirect()->toRoute('feeds');
            } else {
                $container->error = 'Droits insuffisants. Contactez l\'administrateur du serveur.';
                return $this->redirect()->toRoute('home');
            }
        }
    }
    
    public function logoutAction()
    {
        $container = new \Zend\Session\Container('datas');
        unset($container->login);
        unset($container->password);
        unset($container->server);
        unset($container->error);
        unset($container->acc_key);
        return $this->redirect()->toRoute('home');
    }
    
    public function feedsAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $feeds = $this->_soap()->list_feed($container->acc_key);
        $datas = [];
        
        foreach ($feeds as $feed)
        {
            $name = $this->_soap()->get_feed($container->acc_key, $feed);
            $datas[$name] = [
                'id' => $feed,
                'name' => $name
            ];
        }
        ksort($datas);
        return new ViewModel(['feeds' => $datas]);
    }
    
    public function renamefeedAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $feed = $this->params()->fromPost('feed', 0);
        $name = $this->params()->fromPost('name', '');
        if ($name !== '')
            $this->_soap()->edit_feed($container->acc_key, $feed, $name);
        return $this->redirect()->toRoute('feeds');
    }
    
    public function deletefeedAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $feed = $this->params()->fromRoute('id', 0);
        $this->_soap()->delete_feed($container->acc_key, $feed);
        return $this->redirect()->toRoute('feeds');
    }
    
    public function createAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $feed = $this->params()->fromPost('feed');
        $this->_soap()->create_feed($container->acc_key, $feed);
        return $this->redirect()->toRoute('feeds');
    }
    
    public function feedAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $id = $this->params()->fromRoute('id', $container->feed);
        $news = $this->_soap()->get_history($container->acc_key, $id);
        $page = $this->params()->fromRoute('page', 0);
        $container->feed = $id;
        $name = $this->_soap()->get_feed($container->acc_key, $id);
        
        $news_list = array_slice($news, $page * 5, 5);
        $news_content = [];
        foreach ($news_list as $info)
        {
            $comments = $this->_soap()->get_comment($container->acc_key, $info['id']);
            $read = 0;
            $com = [];
            foreach ($comments as $comment)
            {
                $read++;
                if ($comment['content'] !== NULL)
                    $com[] = $comment;
            }
            $news_content[] = [
                'id' => $info['id'],
                'date' => $info['date'],
                'content' => $info['content'],
                'read' => $read,
                'com' => $com
            ];
        }
        
        return new ViewModel([
            'feed' => $name,
            'news' => $news_content,
            'total' => ceil(count($news) / 5),
            'page' => $page,
            'id' => $id
        ]);
    }
    
    public function publishAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $feed = $container->feed;
        $content = $this->params()->fromPost('content', '');
        
        if ($content !== '')
        {
            $this->_soap()->publish($container->acc_key, $feed, $content);
        }
        return $this->redirect()->toRoute('feed');
    }
    
    public function usersAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $page = $this->params()->fromRoute('page');
        
        $users = $this->_soap()->list_acc_keys($container->acc_key);
        $total = count($users);
        $users_list = array_slice($users, 5 * $page, 5);
        $users_final = [];
        foreach ($users_list as $user)
        {
            $users_final[$user] = [
                'datas' => $this->_soap()->get_user($container->acc_key, $user),
                'admin' => $this->_soap()->is_admin_key($user)
            ];
        }
        return new ViewModel([
            'page' => $page,
            'total' => ceil($total / 5),
            'users' => $users_final
        ]);
    }
    
    public function adminuserAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $page = $this->params()->fromRoute('page');
        $user = $this->params()->fromRoute('id');
        $key = $container->acc_key;
        
        $admin = $this->_soap()->is_admin_key($user);
        $this->_soap()->set_user_admin($key, $user, !$admin);
        return $this->redirect()->toRoute('users', ['page' => $page]);
    }
    
    public function deleteuserAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $page = $this->params()->fromRoute('page');
        $user = $this->params()->fromRoute('id');
        $key = $container->acc_key;
        
        $this->_soap()->delete_user($key, $user);
        return $this->redirect()->toRoute('users', ['page' => $page]);
    }
    
    public function updateuserAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $page = $this->params()->fromRoute('page');
        $user = $this->params()->fromRoute('id');
        $key = $container->acc_key;
        $key_info = $this->params()->fromPost('key', '');
        $value_info = $this->params()->fromPost('value', '');
        
        if ($key_info !== '')
            $this->_soap()->set_user_info($key, $user, $key_info, $value_info);
        return $this->redirect()->toRoute('users', ['page' => $page]);
    }
    
    public function createuserAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $page = $this->params()->fromRoute('page');
        $key = $container->acc_key;
        
        if ($key_info !== '')
            $this->_soap()->create_user($key);
        return $this->redirect()->toRoute('users', ['page' => $page]);
    }
    
    public function groupsAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $key = $container->acc_key;
        $groups_list = $this->_soap()->list_group($key);
        $groups = [];
        foreach ($groups_list as $group)
        {
            $groups[] = [
                'id' => $group,
                'name' => $this->_soap()->get_group($key, $group),
                'members' => count($this->_soap()->get_users_for_group($key, $group))
            ];
        }
        return new ViewModel(['groups' => $groups]);
    }
    
    public function creategroupAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $group = $this->params()->fromPost('group');
        $this->_soap()->add_group($container->acc_key, $group);
        return $this->redirect()->toRoute('groups');
    }
    
    public function renamegroupAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $group = $this->params()->fromPost('group', 0);
        $name = $this->params()->fromPost('name', '');
        if ($name !== '')
            $this->_soap()->edit_group($container->acc_key, $group, $name);
        return $this->redirect()->toRoute('groups');
    }
    
    public function deletegroupAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $group = $this->params()->fromRoute('id', 0);
        $this->_soap()->delete_group($container->acc_key, $group);
        return $this->redirect()->toRoute('groups');
    }
    
    public function groupAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $group = $this->params()->fromRoute('id', 0);
        $key = $container->acc_key;
        $name = $this->_soap()->get_group($key, $group);
        $users = $this->_soap()->get_users_for_group($key, $group);
        return new ViewModel(['name' => $name, 'id' => $group, 'users' => $users]);
    }
    
    public function groupadduserAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $group = $this->params()->fromPost('id', 0);
        $user = $this->params()->fromPost('user', '');
        $key = $container->acc_key;
        $this->_soap()->add_member_to_group($key, $group, $user);
        return $this->redirect()->toRoute('group', ['id' => $group]);
    }
    
    public function groupdeleteuserAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $group = $this->params()->fromRoute('group', 0);
        $user = $this->params()->fromRoute('user', '');
        $key = $container->acc_key;
        $this->_soap()->delete_member_from_group($key, $group, $user);
        return $this->redirect()->toRoute('group', ['id' => $group]);
    }
    
    public function feedauthAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $feed = $this->params()->fromRoute('id', 0);
        $groups_list = $this->_soap()->get_groups_for_feed($container->acc_key, $feed);
        $groups = [];
        foreach ($groups_list as $group)
        {
            $groups[] = [
                'id' => $group,
                'name' => $this->_soap()->get_group($container->acc_key, $group)
            ];
        }
        $name = $this->_soap()->get_feed($container->acc_key, $feed);
        $all_groups_list = $this->_soap()->list_group($container->acc_key);
        $all_groups = [];
        foreach ($all_groups_list as $group)
        {
            if (in_array($group, $groups_list))
                continue;
            $all_groups[] = [
                'id' => $group,
                'name' => $this->_soap()->get_group($container->acc_key, $group)
            ];
        }
        return new ViewModel([
            'id' => $feed,
            'name' => $name,
            'groups' => $groups,
            'all_groups' => $all_groups
        ]);
    }
    
    public function feedauthaddAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $groups = $this->params()->fromPost('groups', []);
        $feed = $this->params()->fromPost('feed', 0);
        foreach ($groups as $group)
        {
            $this->_soap()->add_group_for_feed($container->acc_key, $feed, $group);
        }
        return $this->redirect()->toRoute('feed-auth', ['id' => $feed]);
    }
    
    public function feedauthdeleteAction()
    {
        $container = new \Zend\Session\Container('datas');
        if (!isset($container->acc_key))
            return $this->redirect()->toRoute('home');
        $group = $this->params()->fromRoute('group', 0);
        $feed = $this->params()->fromRoute('id', 0);
        $this->_soap()->delete_group_for_feed($container->acc_key, $feed, $group);
        return $this->redirect()->toRoute('feed-auth', ['id' => $feed]);
    }
    
    protected $_soapClient = NULL;
    
    /**
     * 
     * @return \Zend\Soap\Client
     */
    protected function _soap()
    {
        if ($this->_soapClient === NULL)
        {
            $container = new \Zend\Session\Container('datas');
            $this->_soapClient = new \Zend\Soap\Client($container->server . '/?wsdl', ['cache_wsdl' => WSDL_CACHE_NONE]);
        }
        return $this->_soapClient;
    }
}
