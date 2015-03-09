<?php

if (!class_exists('NoExplanation'))
{
    class NoExplanation
    {
        public function get_identification_type()
        {
            return [
                'id_public_desc' => '',
                'id_private_desc' => '',
                'general_desc' => ''
            ];
        }
    }
}

return new NoExplanation();