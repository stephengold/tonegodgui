/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core.utils;

import java.util.UUID;

/**
 *
 * @author t0neg0d
 */
public class UIDUtil {
        /**
         * A private constructor to inhibit instantiation of this class.
         */
        private UIDUtil() {
        }

        public static String getUID() {
		return UUID.randomUUID().toString();
	}
}
