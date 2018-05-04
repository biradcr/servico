/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsul.dao;

import br.edu.ifsul.conexao.Conexao;
import br.edu.ifsul.modelo.Retirada;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ubiratan
 */
public class RetiradaDAO {
    public RetiradaDAO()
    {
    
    }
    
    public boolean inserir(Retirada retirada)
    {
        String sql = "INSERT INTO retiradas(local_inicio,data_hora_inicio,data_hora_fim,destino,local_devolucao,km_inicial,km_final, usuario,veiculo) VALUES(?,?,?,?,?,?,?,?,?)";
        Boolean retorno = false;
        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {
            pst.setString(1, retirada.getLocalInicio());
            pst.setTimestamp(2, retirada.getDataHoraInicio());
            pst.setTimestamp(3, retirada.getDataHoraFim());
            pst.setString(4, retirada.getDestino());
            pst.setString(5, retirada.getLocalDevolucao());
            pst.setInt(6, retirada.getKmInicial());
            pst.setInt(7, retirada.getKmFinal());
            pst.setInt(8, retirada.getUsuario());
            pst.setInt(9, retirada.getVeiculo());
            
            if(pst.executeUpdate()>0)
            {
                retorno = true;
            }
                
            
            
        } catch (SQLException ex) {
            Logger.getLogger(RetiradaDAO.class.getName()).log(Level.SEVERE, null, ex);
            retorno = false;
        }
        
        return retorno;
    
    }
    
    public Retirada getLastRetiradaByPlaca(String placa){
        String sql = "SELECT r.codigo, r.local_inicio, r.data_hora_inicio, r.data_hora_fim, r.destino, r.local_devolucao, \n" +
                      "r.km_inicial, r.km_final, r.usuario, r.veiculo"
                + " FROM retiradas r, veiculos v"
                + " WHERE v.placa = ?"
                + " AND r.veiculo = v.codigo"
                + " ORDER BY r.codigo desc limit 1";
        Retirada item = new Retirada();
        
        /*
            Tive que criar outro tipo de prepared statement porque ele não deixava navegar entre os valores
            que haviam sido carregados na listagem
        */
        PreparedStatement pst = Conexao.getPreparedStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        try {
            
            pst.setString(1, placa);
            
            ResultSet res = pst.executeQuery();
            
            while(res.next()){            
                item.setCodigo(res.getInt("codigo"));
                item.setLocalInicio(res.getString("local_inicio"));
                item.setDataHoraInicio(res.getTimestamp("data_hora_inicio"));
                item.setDataHoraFim(res.getTimestamp("data_hora_fim"));
                item.setDestino(res.getString("destino"));
                item.setLocalDevolucao(res.getString("local_devolucao"));
                item.setKmInicial(res.getInt("km_inicial"));
                item.setKmFinal(res.getInt("km_final"));
                item.setUsuario(res.getInt("usuario"));
                item.setVeiculo(res.getInt("veiculo"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(RetiradaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return item;
    }
    
    public boolean atualizar(Retirada retirada)
    {
        String sql = "UPDATE retirada set local_devolucao=?,km_final=?,data_hora_fim=?";
        Boolean retorno = false;
        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {
            pst.setString(5, retirada.getLocalDevolucao());
            pst.setInt(7, retirada.getKmFinal());            
            pst.setTimestamp(3, retirada.getDataHoraFim());
            if(pst.executeUpdate()>0)
            {
                retorno = true;
            }
                
            
            
        } catch (SQLException ex) {
            Logger.getLogger(RetiradaDAO.class.getName()).log(Level.SEVERE, null, ex);
            retorno = false;
        }
        
        return retorno;
    
    }
    
//    public boolean excluir(Retirada retirada)
//    {
//        String sql = "DELETE FROM retirada where login=?";
//        Boolean retorno = false;
//        PreparedStatement pst = Conexao.getPreparedStatement(sql);
//        try {
//          
//           
//            pst.setString(1, retirada.getLogin());
//            if(pst.executeUpdate()>0)
//            {
//                retorno = true;
//            }
//                
//            
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(RetiradaDAO.class.getName()).log(Level.SEVERE, null, ex);
//            retorno = false;
//        }
//        
//        return retorno;
//    
//    }
    
    public List<Retirada> listar()
    {
         String sql = "SELECT * FROM retiradas";
        List<Retirada> retorno = new ArrayList<Retirada>();
        
        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {
           
            
            ResultSet res = pst.executeQuery();
            while(res.next())
            {
                Retirada item = new Retirada();
                item.setLocalInicio(res.getString("local_inicio"));
                item.setDataHoraInicio(res.getTimestamp("data_hora_inicio"));
                item.setDataHoraFim(res.getTimestamp("data_hora_fim"));
                item.setDestino(res.getString("destino"));
                item.setLocalDevolucao(res.getString("local_devolucao"));
                item.setKmInicial(res.getInt("km_inicial"));
                item.setKmFinal(res.getInt("km_final"));
                item.setUsuario(res.getInt("usuario"));
                item.setVeiculo(res.getInt("veiculo"));
                
                retorno.add(item);
            }
               
            
            
        } catch (SQLException ex) {
            Logger.getLogger(RetiradaDAO.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        return retorno;
    
    
    }
    public Retirada buscar(Retirada retirada)
    {
         String sql = "SELECT * FROM retiradas where codigo=?";
        Retirada retorno = null;
        
        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {
           
            pst.setInt(1, retirada.getCodigo());
            ResultSet res = pst.executeQuery();
            
            if(res.next())
            {
                retorno = new Retirada();
                retorno.setLocalInicio(res.getString("local_inicio"));
                retorno.setDataHoraInicio(res.getTimestamp("data_hora_inicio"));
                retorno.setDataHoraFim(res.getTimestamp("data_hora_fim"));
                retorno.setDestino(res.getString("destino"));
                retorno.setLocalDevolucao(res.getString("local_devolucao"));
                retorno.setKmInicial(res.getInt("km_inicial"));
                retorno.setKmFinal(res.getInt("km_final"));
                retorno.setUsuario(res.getInt("usuario"));
                retorno.setVeiculo(res.getInt("veiculo"));
            }
               
            
            
        } catch (SQLException ex) {
            Logger.getLogger(RetiradaDAO.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        return retorno;
    
    
    }


}
