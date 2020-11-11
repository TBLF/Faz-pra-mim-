package com.example.fpm.moldes;

import com.google.android.gms.maps.model.LatLng;

public class Prestador {

    private String nome;
    private String nomePesquisa;
    private String data_servico;
    private int imagePrestador;
    private LatLng latLngPrestador;
    private String uid;
    private String Tipo;
    private String endereco;



    public Prestador(String nome, String data_servico, Integer imagePrestador) {
        this.nome = nome;
        this.data_servico = data_servico;
        this.imagePrestador = imagePrestador;
    }

    public Prestador(LatLng latLngPrestador, String uid ,String nome) {
        this.latLngPrestador = latLngPrestador;
        this.uid = uid;
        this.nome = nome;
    }
    public Prestador(String endereco,String nome) {
        this.endereco = endereco;
        this.nome = nome;
    }

    public Prestador() {
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getNomePesquisa() {
        return nomePesquisa;
    }

    public void setNomePesquisa(String nomePesquisa) {
        this.nomePesquisa = nomePesquisa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData_servico() {
        return data_servico;
    }

    public void setData_servico(String data_servico) {
        this.data_servico = data_servico;
    }

    public int getImagePrestador() {
        return imagePrestador;
    }

    public void setImagePrestador(int imagePrestador) {
        this.imagePrestador = imagePrestador;
    }

    public LatLng getLatLngPrestador() {
        return latLngPrestador;
    }

    public void setLatLngPrestador(LatLng latLngPrestador) {
        this.latLngPrestador = latLngPrestador;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
