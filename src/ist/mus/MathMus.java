/*
 * Created on May 31, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus;



/**
 * @author Pau Rodriguez-Estivill
 */
public final class MathMus {

    public static int Mod(int num, int mod) {
        if (num < 0)
            return ((mod - 1) * (-num)) % mod;
        return num % mod;
    }
    
    /**
     * Pasa de booleano a int 
     * @param boleano
     * @return devuelve 1 para verdadero y -1 para falso
     */
    public static int booleanToInt(boolean b){
        if (b)
            return 1;
        return -1;
    }
}
