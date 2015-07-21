<?php

if (!class_exists(("PublishAndroid")))
{
    class PublishAndroid
    {
        private $_msg = null;
        private $_ids = [];
        
        public function notify($device_type, $device_key, $app_key, $title, $message)
        {
            if ($device_type !== 'android')
                return false;
            
            if ($this->_msg === null)
                $this->_msg = [
                    'text' => $message,
                    'title' => $title,
                    'vibrate' => 1,
                    'sound' => 1
                ];
            
            if (!isset($this->_ids[$app_key]))
                $this->_ids[$app_key] = [];

            $this->_ids[$app_key][] = $device_key;
            return true;
        }
        
        public function __destruct() {
            foreach ($this->_ids as $key => $ids)
            {
                foreach ($ids as $id)
                {   
                    $fields = [
                        'to' => $id,
                        'notification' => $this->_msg
                    ];
            
                    $headers = [
                        'Authorization: key=' . $key,
                        'Content-type: application/json'
                    ];
                
                    $ch = curl_init();
                    curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
                    curl_setopt( $ch,CURLOPT_POST, true );
                    curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
                    curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
                    curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
                    curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) ); 
                    curl_exec($ch);
                    curl_close($ch);
                }
            }
        }
    }
}

return new PublishAndroid();