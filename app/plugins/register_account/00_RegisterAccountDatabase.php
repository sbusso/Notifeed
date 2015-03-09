<?php

if (!class_exists('RegisterAccountDatabase'))
{
    class RegisterAccountDatabase
    {
        public function register_account($id_public, $id_password)
        {
            $table = new \Zend\Db\TableGateway\TableGateway('User_Infos', include 'app/db.php', new Zend\Db\TableGateway\Feature\RowGatewayFeature('id'), new Zend\Db\ResultSet\ResultSet());

            $login = $table->select([
                'info_key' => 'login',
                'info_value' => $id_public
            ])->current();

            if ($login !== false)
            {
                $acc_key = $login->acc_key;
                $password = $table->select([
                    'acc_key' => $acc_key,
                    'info_key' => 'password'
                ]);
                if (password_verify($id_password, $password->info_value))
                    return $acc_key;
            }
            return false;
        }
    }
}

return new RegisterAccountDatabase();