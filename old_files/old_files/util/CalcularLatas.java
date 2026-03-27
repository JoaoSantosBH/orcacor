package com.jomar.senhorpintor.util;

class CalcularLatas {

        public static int calculoLata18Lt(float numLitrosSeladora) {
            int result = 0;
            do {
                result++;
                numLitrosSeladora -= 18;
            } while (numLitrosSeladora > 18);
            return result;
        }
        public static int calculoLata36Lt(float numLitrosSeladora) {
            int result = 0;
            do {
                result++;
                numLitrosSeladora -= 3.6;
            } while (numLitrosSeladora > 3.6);
            return result;
        }
        public static int calculoLata09Lt(float numLitrosSeladora) {
            int result = 0;
            do {
                result++;
                numLitrosSeladora -= 0.9;
            } while (numLitrosSeladora > 0.9);
            return result;
        }

    }
