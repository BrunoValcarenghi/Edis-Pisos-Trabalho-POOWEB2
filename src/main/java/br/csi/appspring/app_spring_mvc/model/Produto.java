package br.csi.appspring.app_spring_mvc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false, length = 120, unique = true)
    private String modelo;

    @ManyToMany(mappedBy = "produtosFavoritos", fetch = FetchType.LAZY)
    private Set<Usuario> usuariosQueFavoritaram = new HashSet<>();

    public Produto() {
    }

    public Produto(String marca, String modelo) {
        this.marca = marca;
        this.modelo = modelo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Set<Usuario> getUsuariosQueFavoritaram() {
        return usuariosQueFavoritaram;
    }

    public void setUsuariosQueFavoritaram(Set<Usuario> usuariosQueFavoritaram) {
        this.usuariosQueFavoritaram = usuariosQueFavoritaram;
    }
}
