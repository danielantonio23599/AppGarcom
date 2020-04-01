package com.daniel.appgarcom.modelo.beans;

public class Empresa {
        private int empCodigo;
        private String empEmail;
        private String empSenha;
        private String empFantazia;
        private byte[] empLogo;

        public int getEmpCodigo() {
            return empCodigo;
        }

        public void setEmpCodigo(int empCodigo) {
            this.empCodigo = empCodigo;
        }

        public String getEmpEmail() {
            return empEmail;
        }

        public void setEmpEmail(String empEmail) {
            this.empEmail = empEmail;
        }

        public String getEmpSenha() {
            return empSenha;
        }

        public void setEmpSenha(String empSenha) {
            this.empSenha = empSenha;
        }

        public String getEmpFantazia() {
            return empFantazia;
        }

        public void setEmpFantazia(String empFantazia) {
            this.empFantazia = empFantazia;
        }

        public byte[] getEmpLogo() {
            return empLogo;
        }

        public void setEmpLogo(byte[] empLogo) {
            this.empLogo = empLogo;
        }
}
