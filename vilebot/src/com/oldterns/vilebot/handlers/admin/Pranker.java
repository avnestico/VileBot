/**
 * Copyright (C) 2014 Oldterns
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package com.oldterns.vilebot.handlers.admin;

import ca.szc.keratin.bot.KeratinBot;
import ca.szc.keratin.bot.annotation.AssignedBot;
import ca.szc.keratin.bot.annotation.HandlerContainer;
import ca.szc.keratin.core.event.message.recieve.ReceivePrivmsg;
import com.oldterns.vilebot.db.GroupDB;
import com.oldterns.vilebot.util.Sessions;
import net.engio.mbassy.listener.Handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@HandlerContainer
public class Pranker
{
    private static String target = "";

    private static final Pattern nounPattern = Pattern.compile( "\\S+" );

    private static final Pattern targetPattern = Pattern.compile( "!admin (un|)target (" + nounPattern + ")\\s*" );

    @AssignedBot
    private KeratinBot bot;

    @Handler
    private void getTarget( ReceivePrivmsg event )
    {
        Matcher targetMatcher = targetPattern.matcher( event.getText() );
        String username = Sessions.getSession( event.getSender() );

        if ( targetMatcher.matches() && GroupDB.isAdmin(username) )
        {
            String mode = targetMatcher.group( 1 );
            String NewTarget = targetMatcher.group( 2 );

            boolean untarget = "un".equals( mode );

            if ( Pranker.target.equals( NewTarget ) )
            {
                if ( untarget )
                {
                    Pranker.target = "";
                    event.reply ( NewTarget + " is no longer being targeted.");
                }
                else
                {
                    event.reply( NewTarget + " is still our target. Excellent.");
                }
            }
            else if (Pranker.target.equals( "" ) )
            {
                if ( untarget )
                {
                    event.reply( "There is no current target." );
                }
                else
                {
                    Pranker.target = NewTarget;
                    event.reply("Target " + NewTarget + " acquired.");
                }
            }
            else
            {
                if ( untarget )
                {
                    event.reply("Current target is " + Pranker.target + ", not " + NewTarget + ".");
                }
                else
                {
                    event.reply("Current target is " + Pranker.target + ". !untarget your old victim first.");
                }

            }
        }
    }

    public static String prankee(String baseNick)
    {
        if ( Pranker.target.equals( "" ) )
        {
            return baseNick;
        }
        else
        {
            return Pranker.target;
        }
    }
}
