
package com.example.bluetoothkeyboard;

import java.lang.System;

public class KeyPacket 
{

    private static final int KEY_DOWN  = 0;
    private static final int KEY_UP    = 1;

    public  static final int MOD_CTRL  = 0;
    public  static final int MOD_META  = 0;
    public  static final int MOD_SUPER = 0;
    public  static final int MOD_SHIFT = 0;

    private char m_keycode;
    private byte m_operation;
    private byte m_modifiers;

    public KeyPacket()
    {
        m_keycode   = 0;
        m_operation = 0x0;
        m_modifiers = 0x0;

    }
    public KeyPacket(byte[] in_data)
    {
        System.arraycopy( m_operation, 0, in_data, 0, 1);
        System.arraycopy( m_modifiers, 0, in_data, 1, 1);
        System.arraycopy( m_keycode,   0, in_data, 4, 4);
    }

    public byte[] to_bytes()
    {
        byte[] out = new byte[4 + 1 + 1];
        System.arraycopy(out, 0, m_operation, 0, 1);
        System.arraycopy(out, 1, m_modifiers, 0, 1);
        System.arraycopy(out, 2, m_keycode,   0, 4);
        return out;
    }

    public char get_key()
    {
        return this.m_keycode;
    }

    public byte get_modifiers()
    {
        return this.m_modifiers;
    }
    
    public byte get_operation()
    {
        return this.m_operation;
    }

}

