<?php

class User {
    /**
     * Register a new device for an user. Return true if ok, false if the device already exists.
     * 
     * @param string $id_device
     * @param string $type_device
     * @param string $app_key Application key. The meaning of this value may vary depending of type_device.
     * @param string $acc_key
     * @return boolean
     */
    public function register_device($id_device, $type_device, $app_key, $acc_key)
    {
        $table = new Zend\Db\TableGateway\TableGateway('User_Device', include 'app/db.php');
        
        try {
            $table->insert(['device_type' => $type_device, 'device_key' => $id_device, 'acc_key' => $acc_key, 'app_key' => $app_key]);
            return true;
        } catch (Exception $e)
        {
            return false;
        }
    }
    
    /**
     * Identify an user and send back it's account key.
     * 
     * @param string $id_public
     * @param string $id_private
     * @return string
     */
    public function register_account($id_public, $id_private)
    {
        $plugins = glob('app/plugins/register_account/*.php');
        
        foreach ($plugins as $plugin)
        {
            $tool = include $plugin;
            if (($acc_key = $tool->register_account($id_public, $id_private)) !== false)
            {
                return $acc_key;
            }
        }
        return 'Invalid username or password';
    }
    
    /**
     * Return informations about the identification fields for this server
     * 
     * @return string
     */
    public function get_identification_type()
    {
        $plugins = glob('app/plugins/get_identification_type/*.php');
        
        foreach ($plugins as $plugin)
        {
            $tool = include $plugin;
            return $tool->get_identification_type();
        }
        return [];
    }
    
    /**
     * Remove a device registered for an user
     * 
     * @param string $device_id
     * @param string $acc_key
     * @return boolean
     */
    public function unregister_device($device_id, $acc_key)
    {
        $table = new Zend\Db\TableGateway\TableGateway('User_Device', include 'app/db.php');
        
        $table->delete([
            'acc_key' => $acc_key,
            'device_key' => $device_id
        ]);
        return true;
    }
    
    /**
     * Get list of feeds available to user
     * 
     * @param string $acc_key
     * @return array
     */
    public function list_feed($acc_key)
    {
        $adapter = include 'app/db.php';
        
        if (!$this->_isAdmin($acc_key))
            $stmt = $adapter->createStatement('SELECT f.feed_id FROM Feed_Group f LEFT JOIN User_Group ug ON ug.group_id = f.group_id WHERE ug.acc_key = ?');
        else
            $stmt = $adapter->createStatement('SELECT id AS feed_id FROM Feed WHERE 1');
        $vals = $stmt->execute([$acc_key]);
        $result = [];
        foreach ($vals as $val)
        {
            $result[] = $val['feed_id'];
        }
        return $result;
    }
    
    private function _canUserFollowFeed($acc_key, $feed_id)
    {
        $adapter = include 'app/db.php';
        
        if ($this->_isAdmin($acc_key))
            return true;
            
        $stmt = $adapter->createStatement('SELECT f.feed_id FROM Feed_Group f LEFT JOIN User_Group ug ON ug.group_id = f.group_id WHERE ug.acc_key = ? AND f.feed_id = ?');
        $vals = $stmt->execute([$acc_key, $feed_id]);
        return (count($vals) > 0);
    }
    
    /**
     * Check if a given user is following a given feed
     * 
     * @param string $acc_key
     * @param int $feed_id
     * @return boolean
     */
    public function is_following_feed($acc_key, $feed_id)
    {
        $adapter = include 'app/db.php';
        
        $stmt = $adapter->createStatement('SELECT feed_id FROM User_Feed WHERE feed_id = ? AND acc_key = ?');
        $vals = $stmt->execute([$feed_id, $acc_key]);
        return (count($vals) > 0);
    }
    
    /**
     * Retrieve notifications from a feed
     * 
     * @param string $acc_key
     * @param int $feed_id
     * @return array
     */
    public function get_history($acc_key, $feed_id)
    {
        if ($this->_canUserFollowFeed($acc_key, $feed_id))
        {
            $adapter = include 'app/db.php';
            
            $stmt = $adapter->createStatement('SELECT `id`, `content`, `date` FROM `Notif` WHERE `feed_id` = ? ORDER BY `date` DESC');
            $vals = $stmt->execute([$feed_id]);
            $valsArr = [];
            foreach ($vals as $val)
                $valsArr[] = $val;
            return $valsArr;
        }
        return [];
    }
    
    /**
     * Retrieve the name of a feed
     * 
     * @param string $acc_key
     * @param int $feed_id
     * @return string
     */
    public function get_feed($acc_key, $feed_id)
    {
        if ($this->_canUserFollowFeed($acc_key, $feed_id))
        {
            $adapter = include 'app/db.php';
            
            $stmt = $adapter->createStatement('SELECT `name` FROM `Feed` WHERE `id` = ?');
            $vals = $stmt->execute([$feed_id]);
            return $vals->current()['name'];
        }
        return '';
    }
    
    /**
     * Set an user as following a feed
     * 
     * @param string $acc_key
     * @param int $feed_id
     * @return boolean
     */
    public function follow_feed($acc_key, $feed_id)
    {
        if ($this->_canUserFollowFeed($acc_key, $feed_id))
        {
            $table = new Zend\Db\TableGateway\TableGateway('User_Feed', include 'app/db.php');
            
            $table->insert([
                'acc_key' => $acc_key,
                'feed_id' => $feed_id
            ]);
            return true;
        }
        return false;
    }
    
    /**
     * Remove a feed as followed by an user
     * 
     * @param string $acc_key
     * @param int $feed_id
     * @return boolean
     */
    public function unfollow_feed($acc_key, $feed_id)
    {
        $adapter = include 'app/db.php';
        
        $stmt = $adapter->createStatement('DELETE FROM User_Feed WHERE acc_key = ? AND feed_id = ?');
        $stmt->execute([$acc_key, $feed_id]);
        return true;
    }
    
    /**
     * Add a comment to a notification
     * 
     * @param string $acc_key
     * @param int $notif_id
     * @param string|NULL $content
     * @return boolean
     */
    public function comment($acc_key, $notif_id, $content)
    {
        $adapter = include 'app/db.php';
        
        $notifTable = new \Zend\Db\TableGateway\TableGateway('Notif', $adapter, new Zend\Db\TableGateway\Feature\RowGatewayFeature('id'), new Zend\Db\ResultSet\ResultSet());
        $notif = $notifTable->select(['id' => $notif_id])->current();
        
        if ($notif !== false && $this->_canUserFollowFeed($acc_key, $notif->feed_id))
        {
            $commentTable = new \Zend\Db\TableGateway\TableGateway('Comment', $adapter, new Zend\Db\TableGateway\Feature\RowGatewayFeature('id'), new Zend\Db\ResultSet\ResultSet());
            $commentTable->insert([
                'acc_key' => $acc_key,
                'content' => is_null($content) || empty($content) ? NULL : $content,
                'notif_id' => $notif_id
            ]);
            return true;
        }
        return false;
    }
    
    private function _isAdmin($acc_key)
    {
        $adapter = include 'app/db.php';
        
        $userTable = new \Zend\Db\TableGateway\TableGateway('User', $adapter, new Zend\Db\TableGateway\Feature\RowGatewayFeature('acc_key'), new Zend\Db\ResultSet\ResultSet());
        $user = $userTable->select(['acc_key' => $acc_key])->current();
        
        return ($user !== false && $user->admin);
    }
    
    /**
     * Check if a given key is an admin key.
     * 
     * @param string $acc_key_admin
     * @return boolean
     */
    public function is_admin_key($acc_key_admin)
    {
        return ($this->_isAdmin($acc_key_admin));
    }
    
    /**
     * List every acc_key. Admin function.
     * 
     * @param string $acc_key_admin
     * @return array
     */
    public function list_acc_keys($acc_key_admin)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $adapter = include 'app/db.php';
            $stmt = $adapter->createStatement('SELECT acc_key FROM User WHERE 1');
            $list = [];
            $vals = $stmt->execute();
            foreach ($vals as $val)
                $list[] = $val['acc_key'];
            return $list;
        }
        return [];
    }
    
    /**
     * Retrieve datas about an user. Admin function.
     * 
     * @param string $acc_key_admin
     * @param string $acc_key
     * @return array
     */
    public function get_user($acc_key_admin, $acc_key)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $adapter = include 'app/db.php';
            $userTable = new \Zend\Db\TableGateway\TableGateway('User', $adapter, new Zend\Db\TableGateway\Feature\RowGatewayFeature('acc_key'), new Zend\Db\ResultSet\ResultSet());
            $user = $userTable->select(['acc_key' => $acc_key])->current();
            
            $datas = [];
            
            $infosStatement = $adapter->createStatement('SELECT info_key, info_value FROM User_Infos WHERE acc_key = ?');
            $infos = $infosStatement->execute([$acc_key]);
            
            foreach ($infos as $info)
                $datas[$info['info_key']] = $info['info_value'];
            return $datas;
        }
        return [];
    }
    
    /**
     * Add a new group. Admin function.
     * 
     * @param string $acc_key_admin
     * @param string $group_name
     * @return int
     */
    public function add_group($acc_key_admin, $group_name)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $groupsTable = new \Zend\Db\TableGateway\TableGateway('Group', include 'app/db.php');
            try {
                $groupsTable->insert([
                    'name' => $group_name
                ]);
                return $groupsTable->lastInsertValue;
            } catch (Exception $e) {
                return -1;
            }
        }
        return -1;
    }
    
    /**
     * Return the name of a group. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $group_id
     * @return string
     */
    public function get_group($acc_key_admin, $group_id)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $groupsTable = new \Zend\Db\TableGateway\TableGateway('Group', include 'app/db.php', new Zend\Db\TableGateway\Feature\RowGatewayFeature('id'), new Zend\Db\ResultSet\ResultSet());
            $group = $groupsTable->select([
                'id' => $group_id
            ])->current();
            return $group !== false ? $group->name : '';
        }
        return '';
    }
    
    /**
     * Return the list of groups ids. Admin function.
     * 
     * @param string $acc_key_admin
     * @return array
     */
    public function list_group($acc_key_admin)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $adapter = include 'app/db.php';
            $stmt = $adapter->createStatement('SELECT id FROM `Group` WHERE 1');
            $list = [];
            $vals = $stmt->execute();
            
            foreach ($vals as $val)
                $list[] = $val['id'];
            return $list;
        }
        return [];
    }
    
    /**
     * Update the name of a group. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $group_id
     * @param string $name
     * @return boolean
     */
    public function edit_group($acc_key_admin, $group_id, $name)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $adapter = include 'app/db.php';
            
            $stmt = $adapter->createStatement('UPDATE `Group` SET name = ? WHERE id = ?');
            $stmt->execute([$name, $group_id]);
            return true;
        }
        return false;
    }
    
    /**
     * Delete a group. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $group_id
     * @return boolean
     */
    public function delete_group($acc_key_admin, $group_id)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $adapter = include 'app/db.php';
            
            $stmt = $adapter->createStatement('DELETE FROM `Group` WHERE id = ?');
            $stmt->execute([$group_id]);
            return true;
        }
        return false;
    }
    
    /**
     * Add a member to an existing group. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $group_id
     * @param string $acc_key
     * @return boolean
     */
    public function add_member_to_group($acc_key_admin, $group_id, $acc_key)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            try {
                $table = new \Zend\Db\TableGateway\TableGateway('User_Group', include 'app/db.php');
                $table->insert([
                    'acc_key' => $acc_key,
                    'group_id' => $group_id
                ]);
                return true;
            } catch (Exception $ex) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Remove a member from an existing group. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $group_id
     * @param string $acc_key
     * @return boolean
     */
    public function delete_member_from_group($acc_key_admin, $group_id, $acc_key)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            try {
                $table = new \Zend\Db\TableGateway\TableGateway('User_Group', include 'app/db.php');
                $table->delete([
                    'acc_key' => $acc_key,
                    'group_id' => $group_id
                ]);
                return true;
            } catch (Exception $ex) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Retrieve list of groups an user is currently in. Admin function.
     * 
     * @param string $acc_key_admin
     * @param string $acc_key
     * @return array
     */
    public function get_groups_for_user($acc_key_admin, $acc_key)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $adapter = include 'app/db.php';
            $stmt = $adapter->createStatement('SELECT group_id FROM User_Group WHERE acc_key = ?');
            $vals = $stmt->execute([$acc_key]);
            $list = [];
            foreach ($vals as $val)
                $list[] = $val['group_id'];
            return $list;
        }
        return [];
    }
    
    /**
     * Retrieve list of users in a given group. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $group_id
     * @return array
     */
    public function get_users_for_group($acc_key_admin, $group_id)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $adapter = include 'app/db.php';
            $stmt = $adapter->createStatement('SELECT acc_key FROM User_Group WHERE group_id = ?');
            $vals = $stmt->execute([$group_id]);
            $list = [];
            foreach ($vals as $val)
                $list[] = $val['acc_key'];
            return $list;
        }
        return [];
    }
    
    /**
     * Create a new feed with no auth. Admin function.
     * 
     * @param string $acc_key_admin
     * @param string $name
     * @return int
     */
    public function create_feed($acc_key_admin, $name)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $table = new \Zend\Db\TableGateway\TableGateway('Feed', include 'app/db.php');
            try {
                $table->insert([
                    'name' => $name
                ]);
                return $table->lastInsertValue;
            } catch (Exception $e)
            {
                return -1;
            }
        }
        return -1;
    }
    
    /**
     * Update the name of a feed. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $feed_id
     * @param string $name
     * @return boolean
     */
    public function edit_feed($acc_key_admin, $feed_id, $name)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $table = new \Zend\Db\TableGateway\TableGateway('Feed', include 'app/db.php');
            try {
                $table->update([
                    'name' => $name
                ], ['id' => $feed_id]);
                return true;
            } catch (Exception $e)
            {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Delete a feed. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $feed_id
     * @return boolean
     */
    public function delete_feed($acc_key_admin, $feed_id)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $table = new \Zend\Db\TableGateway\TableGateway('Feed', include 'app/db.php');
            try {
                $table->delete(['id' => $feed_id]);
                return true;
            } catch (Exception $e)
            {
                return false;
            }
        }
        return false;
    }
    
    /**
     * List groups allowed to see a given feed. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $feed_id
     * @return array
     */
    public function get_groups_for_feed($acc_key_admin, $feed_id)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $adapter = include 'app/db.php';
            $stmt = $adapter->createStatement('SELECT group_id FROM Feed_Group WHERE feed_id = ?');
            $vals = $stmt->execute([$feed_id]);
            $list = [];
            foreach ($vals as $val)
                $list[] = $val['group_id'];
            return $list;
        }
        return [];
    }
    
    /**
     * Allow a group to view a specific feed. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $feed_id
     * @param int $group_id
     * @return boolean
     */
    public function add_group_for_feed($acc_key_admin, $feed_id, $group_id)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $table = new \Zend\Db\TableGateway\TableGateway('Feed_Group', include 'app/db.php');
            try {
                $table->insert([
                    'feed_id' => $feed_id,
                    'group_id' => $group_id
                ]);
                return true;
            } catch (Exception $ex) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Remove permission for a group to see a feed. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $feed_id
     * @param int $group_id
     * @return boolean
     */
    public function delete_group_for_feed($acc_key_admin, $feed_id, $group_id)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $table = new \Zend\Db\TableGateway\TableGateway('Feed_Group', include 'app/db.php');
            try {
                $table->delete([
                    'feed_id' => $feed_id,
                    'group_id' => $group_id
                ]);
                return true;
            } catch (Exception $ex) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Create a new user and return it's acc_key. Admin function.
     * 
     * @param string $acc_key_admin
     * @return string
     */
    public function create_user($acc_key_admin)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $acc_key = substr(bin2hex(openssl_random_pseudo_bytes(10)), 0, 10);
            $tableUsers = new \Zend\Db\TableGateway\TableGateway('User', include 'app/db.php');
            
            try
            {
                $tableUsers->insert(['acc_key' => $acc_key]);
                return $acc_key;
            } catch (Exception $ex) {
                return '';
            }
        }
        return '';
    }
    
    /**
     * Update a non-mandatory field for an user. Admin function.
     * 
     * @param string $acc_key_admin
     * @param string $acc_key
     * @param string $info_key
     * @param string $info_value
     * @return boolean
     */
    public function set_user_info($acc_key_admin, $acc_key, $info_key, $info_value)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $adapter = include 'app/db.php';
            $stmt = $adapter->createStatement('INSERT INTO User_Infos (acc_key, info_key, info_value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE info_value = ?');
            $stmt->execute([$acc_key, $info_key, $info_value, $info_value]);
            return true;
        }
        return false;
    }
    
    /**
     * Update an user's admin status. Admin function.
     * 
     * @param string $acc_key_admin
     * @param string $acc_key
     * @param boolean $admin_status
     * @return boolean
     */
    public function set_user_admin($acc_key_admin, $acc_key, $admin_status)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $tableUsers = new \Zend\Db\TableGateway\TableGateway('User', include 'app/db.php');
            
            $tableUsers->update(['admin' => $admin_status], ['acc_key' => $acc_key]);
            return true;
        }
        return false;
    }
    
    /**
     * Delete an user. Admin function.
     * 
     * @param string $acc_key_admin
     * @param string $acc_key
     * @return boolean
     */
    public function delete_user($acc_key_admin, $acc_key)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $tableUsers = new \Zend\Db\TableGateway\TableGateway('User', include 'app/db.php');
            
            $tableUsers->delete(['acc_key' => $acc_key]);
            return true;
        }
        return false;
    }
    
    /**
     * Get the list of comments on a notification. NULL content means "read". Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $notif_id
     * @return array
     */
    public function get_comment($acc_key_admin, $notif_id)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $adapter = include 'app/db.php';
            $stmt = $adapter->createStatement('SELECT acc_key, content FROM Comment WHERE notif_id = ? ORDER BY id DESC');
            $listObj = $stmt->execute([$notif_id]);
            $list = [];
            foreach ($listObj as $elem)
                $list[] = $elem;
            return $list;
        }
        return [];
    }
    
    /**
     * Publish a notification on a feed. Admin function.
     * 
     * @param string $acc_key_admin
     * @param int $feed_id
     * @param string $message
     * @return boolean
     */
    public function publish($acc_key_admin, $feed_id, $message)
    {
        if ($this->_isAdmin($acc_key_admin))
        {
            $adapter = include 'app/db.php';
            $tableNotif = new \Zend\Db\TableGateway\TableGateway('Notif', $adapter);
            $tableFeed = new \Zend\Db\TableGateway\TableGateway('Feed', $adapter, new Zend\Db\TableGateway\Feature\RowGatewayFeature('id'), new Zend\Db\ResultSet\ResultSet());
            $feed = $tableFeed->select(['id' => $feed_id])->current();
            
            if ($feed === false)
                return false;
            
            $message = substr($message, 0, 140);
            $tableNotif->insert([
                'feed_id' => $feed_id,
                'content' => $message
            ]);
            $plugins = glob('app/plugins/publish/*.php');
            
            $stmt = $adapter->createStatement('SELECT d.device_key, d.device_type, d.app_key FROM User_Device d LEFT JOIN User_Feed f ON f.acc_key = d.acc_key WHERE f.feed_id = ?');
            $list = $stmt->execute([$feed_id]);
            
            foreach ($list as $device)
            {
                foreach ($plugins as $plugin)
                {
                    $tool = include $plugin;
                    if (($acc_key = $tool->notify($device['device_type'], $device['device_key'], $device['app_key'], $feed->name, $message)))
                        break;
                }
            }
            return true;
        }
        return false;
    }
}
