/*
 * Copyright (c) 2024 SUSE LLC
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 */

package com.suse.manager.webui.utils.token;

/**
 * An exception happening while parsing a token
 */
public class TokenParsingException extends TokenException {

    /**
     * Builds an instance with the given message
     * @param message the message
     */
    public TokenParsingException(String message) {
        super(message);
    }

    /**
     * Builds an instance with the given cause and message
     * @param message the message
     * @param cause what caused this exception
     */
    public TokenParsingException(String message, Exception cause) {
        super(message, cause);
    }
}
