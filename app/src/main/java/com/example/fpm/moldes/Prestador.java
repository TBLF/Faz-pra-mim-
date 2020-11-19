package com.example.fpm.moldes;

import com.google.android.gms.maps.model.LatLng;

public class Prestador {

    private String nome;
    private String nomePesquisa;
    private String data_servico;
    private int imagePrestador;
    private LatLng latLngPrestador;
    private String uid;
    private int tipo;
    private String endereco;
    private double latitude;
    private double longitude;
    private String descricao;
    private String email;
    private String senha;
    private String telefone;
    private int idade;



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
    public Prestador(String endereco,String nome,LatLng latLngPrestador) {
        this.endereco = endereco;
        this.nome = nome;
        this.latLngPrestador = latLngPrestador;
    }

    public Prestador() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getNomePesquisa() {
        return nomePesquisa;
    }

    public void setNomePesquisa(String nomePesquisa) {
        this.nomePesquisa = nomePesquisa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
