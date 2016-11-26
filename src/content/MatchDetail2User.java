package content;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MatchDetail2User
 */
@WebServlet("/MatchDetail2User")
public class MatchDetail2User extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  public MatchDetail2User() {
    super();
    // TODO Auto-generated constructor stub
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    try {
      long id = Long.valueOf(request.getParameter("id"));
      // long id = 1762832017L;     
      StringBuilder  slotsDire = new StringBuilder(),slotsRadiant = new StringBuilder(), matchOverView = new StringBuilder();
      // создаём пустой коннект к базе
      Connection connect = null;
      try {
        // Загружаем драйвер
        Class.forName("org.postgresql.Driver");
        // System.out.println("Драйвер подключен");
        // Создаём соединение
        connect = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Dota2stat", "postgres", "qwerty");
        // System.out.println("Соединение установлено");
        PreparedStatement statementMatch = null, statementSlot = null;
        statementMatch = connect.prepareStatement("select * from matches where match_id = ?");
        statementSlot = connect
            .prepareStatement("select * from all_player_slots where match_id = ? order by slot_number");
        ResultSet rs = null, rs1 = null;
        statementMatch.setLong(1, id);
        statementSlot.setLong(1, id);
        rs = statementMatch.executeQuery();
        rs1 = statementSlot.executeQuery();
        // первая зона - общая информация о матче
        if (rs.next()) {
          matchOverView.append("<table width = 100%>");
            matchOverView.append("<th>Match number</th>");
            matchOverView.append("<th>Duration</th>");
            matchOverView.append("<th>Game mode</th>");
            matchOverView.append("<th>Date</th>");
            matchOverView.append("<tr align = \"center\">");
              matchOverView.append("<td>");
                matchOverView.append(rs.getLong(1));
              matchOverView.append("</td>");
              matchOverView.append("<td>");
                matchOverView.append(rs.getInt(2) / 60 + ":" + rs.getInt(2) % 60);
              matchOverView.append("</td>");
              matchOverView.append("<td>");
                matchOverView.append(rs.getInt(3));
              matchOverView.append("</td>");
              matchOverView.append("<td>");
              matchOverView.append("</td>");
            matchOverView.append("</tr>");
          matchOverView.append("</table>");
         /* matchOverView += "<table width = 100%><th>Match number</th><th>Duration</th><th>Game mode</th><th>Date</th><tr align = \"center\"><td>"
              + rs.getLong(1) + "</td><td>" + rs.getInt(2) / 60 + ":" + rs.getInt(2) % 60 + "</td><td>" + rs.getInt(3)
              + "</td><td>" + "</td><td></table>";
              */
        }
        int num;
        StringBuilder perkHero = new StringBuilder(), perkLast = new StringBuilder(), perkKill = new StringBuilder(), perkTower = new StringBuilder();
        long topHero, topLast, topKill, topTower;
        topHero = rs.getLong(5);//игрок нанёсший наибольшее количество урона по героям противника
        topLast = rs.getLong(6);//добивший больше всех крипов
        topKill = rs.getLong(7);//наиболее активный убиватор(с учётом ассистов)
        topTower = rs.getLong(8);//игрок нанёсший наибольшее количество урона по строениям противника
        
        // заполняем слоты игроков
        while (rs1.next()) {
          if(rs1.getLong(3)==topHero){
            perkHero.append("<table width = 100%, align = \"center\", border = 1px, cols = \"1\">");
              perkHero.append("<tr align = \"center\">");
                perkHero.append("<td>");
                  perkHero.append(topHero);
                perkHero.append("</td>");
              perkHero.append("</tr>");
              perkHero.append("<tr align = \"center\">");
                perkHero.append("<td>");
                  perkHero.append(rs1.getInt(2));
                perkHero.append("</td>");
              perkHero.append("</tr>");
              perkHero.append("<tr align = \"center\">");
                perkHero.append("<td>");
                  perkHero.append(rs1.getLong(14) + "<br>" + " damage dealt");
                perkHero.append("</td>");
              perkHero.append("</tr>");
              perkHero.append("<tr align = \"center\">");
                perkHero.append("<td>");
                  perkHero.append(rs1.getLong(15) + "% of command <br> damage");
                perkHero.append("</td>");
              perkHero.append("</tr>");
            perkHero.append("</table>");
          }
          if(rs1.getLong(3)==topLast){
            perkLast.append("<table width = 100%, align = \"center\", border = 1px>");
              perkLast.append("<tr align = \"center\">");
                perkLast.append("<td>");
                  perkLast.append(topLast);
                perkLast.append("</td>");
              perkLast.append("</tr>");
              perkLast.append("<tr align = \"center\">");
                perkLast.append("<td>");
                  perkLast.append(rs1.getInt(2));
                perkLast.append("</td>");
              perkLast.append("</tr>");
              perkLast.append("<tr align = \"center\">");
                perkLast.append("<td>");
                  perkLast.append(rs1.getLong(18) + "<br>creeps killed");
                perkLast.append("</td>");
              perkLast.append("</tr>");
              perkLast.append("<tr align = \"center\">");
                perkLast.append("<td>");
                  perkLast.append(rs1.getLong(19) + "<br>gold earned");
                perkLast.append("</td>");
              perkLast.append("</tr>");
            perkLast.append("</table>");
          }
          if(rs1.getLong(3)==topKill){
            perkKill.append("<table width = 100%, align = \"center\", border = 1px>");
              perkKill.append("<tr align = \"center\">");
                perkKill.append("<td>");
                  perkKill.append(topKill);
                perkKill.append("</td>");
              perkKill.append("</tr>");
              perkKill.append("<tr align = \"center\">");
                perkKill.append("<td>");
                  perkKill.append(rs1.getInt(2));
                perkKill.append("</td>");
              perkKill.append("</tr>");
              perkKill.append("<tr align = \"center\">");
                perkKill.append("<td>");
                  perkKill.append(rs1.getInt(5) + " kills<br>" + rs1.getInt(7) + " assists");
                perkKill.append("</td>");
              perkKill.append("</tr>");
              perkKill.append("<tr align = \"center\">");
                perkKill.append("<td>");
                  perkKill.append(rs1.getLong(21) + "% <br>of command kills");
                perkKill.append("</td>");
              perkKill.append("</tr>");
            perkKill.append("</table>");
          }
          if(rs1.getLong(3)==topTower){
            perkTower.append("<table width = 100%, align = \"center\", border = 1px>");
              perkTower.append("<tr align = \"center\">");
                perkTower.append("<td>");
                  perkTower.append(topTower);
                perkTower.append("</td>");
              perkTower.append("</tr>");
              perkTower.append("<tr align = \"center\">");
                perkTower.append("<td>");
                  perkTower.append(rs1.getInt(2));
                perkTower.append("</td>");
              perkTower.append("</tr>");
              perkTower.append("<tr align = \"center\">");
                perkTower.append("<td>");
                  perkTower.append(rs1.getInt(16) + " tower <br> damage dealt");
                perkTower.append("</td>");
              perkTower.append("</tr>");
              perkTower.append("<tr align = \"center\">");
                perkTower.append("<td>");
                  perkTower.append(rs1.getLong(17) + "% <br>of command damage");
                perkTower.append("</td>");
              perkTower.append("</tr>");
            perkTower.append("</table>");
          }
          num = rs1.getInt(4);
          // команда светлых
          if (num < 5){      
            slotsRadiant.append("<div>");
              slotsRadiant.append("<table border = 2px, cellspacing = 5px, width = 100%>");
                slotsRadiant.append("<tr align = \"center\">");
                  slotsRadiant.append("<td width = 30%>");
                    slotsRadiant.append(rs1.getInt(2)); //герой
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td width = 30%>");
                    slotsRadiant.append(rs1.getLong(3)); //аккаунт
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td width = 30%>");
                    slotsRadiant.append("<table>");
                      slotsRadiant.append("<tr align = \"center\">");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(5));//убийства
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(6));//смерти
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(7));//ассисты
                        slotsRadiant.append("</td>");
                      slotsRadiant.append("</tr>");
                    slotsRadiant.append("</table>");
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td width = 10%>");
                    slotsRadiant.append(rs1.getLong(14)/(rs.getInt(2)/60));//урон героям в минуту
                  slotsRadiant.append("</td>");
                slotsRadiant.append("</tr>");
                slotsRadiant.append("<tr align = \"center\">");
                  slotsRadiant.append("<td>");
                    slotsRadiant.append("<table border = 1px, width = 100%>");//вещи в инвентаре героя (6штук)
                      slotsRadiant.append("<tr>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(8));
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(9));
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(10));
                        slotsRadiant.append("</td>");
                      slotsRadiant.append("</tr>");
                      slotsRadiant.append("<tr>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(11));
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(12));
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(13));
                        slotsRadiant.append("</td>");
                    slotsRadiant.append("</tr>");
                    slotsRadiant.append("</table>");
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td>");
                    slotsRadiant.append("<table width = 100%>");
                      slotsRadiant.append("<tr align = \"center\">");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(19));//золото за весь матч
                        slotsRadiant.append("</td>");
                      slotsRadiant.append("</tr>");
                      slotsRadiant.append("<tr align = \"center\">");
                        slotsRadiant.append("<td>");
                        slotsRadiant.append(rs1.getLong(20));//опыт за весь матч
                        slotsRadiant.append("</td>");
                      slotsRadiant.append("</tr>");
                    slotsRadiant.append("</table>");
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td>");
                    slotsRadiant.append(rs1.getInt(21) + "%");//% участия в убийствах команды
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td>");
                    slotsRadiant.append("<table width = 100%>");
                      slotsRadiant.append("<tr align = \"center\">");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(16)/(rs.getInt(2)/60));
                        slotsRadiant.append("</td>");
                      slotsRadiant.append("</tr>");
                      slotsRadiant.append("<tr align = \"center\">");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(new BigDecimal((rs1.getLong(18)/(rs.getInt(2)/60f))) .setScale(1, RoundingMode.HALF_UP).floatValue());
                        slotsRadiant.append("</td>");
                    slotsRadiant.append("</tr>");
                    slotsRadiant.append("</table>");
                  slotsRadiant.append("</td>");
                slotsRadiant.append("</tr>");
              slotsRadiant.append("</table>");
            slotsRadiant.append("</div");
          }
            // команда тёмных
            if (num > 127){
              slotsDire.append("<div>");
              slotsDire.append("<table border = 2px, cellspacing = 5px, width = 100%>");
                slotsDire.append("<tr align = \"center\">");
                  slotsDire.append("<td width = 10%>");
                    slotsDire.append(rs1.getLong(14)/(rs.getInt(2)/60));//урон героям в минуту
                  slotsDire.append("</td>");
                  slotsDire.append("<td width = 30%>");
                    slotsDire.append("<table>");
                      slotsDire.append("<tr align = \"center\">");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(5));//убийства
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(6));//смерти
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(7));//ассисты
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                    slotsDire.append("</table>");
                  slotsDire.append("</td>");
                  slotsDire.append("<td width = 30%>");
                    slotsDire.append(rs1.getLong(3)); //аккаунт
                  slotsDire.append("</td>");
                  slotsDire.append("<td width = 30%>");
                    slotsDire.append(rs1.getInt(2)); //герой
                  slotsDire.append("</td>");
                slotsDire.append("</tr>");
                slotsDire.append("<tr align = \"center\">");
                  slotsDire.append("<td>");
                    slotsDire.append("<table width = 100%>");
                      slotsDire.append("<tr align = \"center\">");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(16)/(rs.getInt(2)/60));
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                      slotsDire.append("<tr align = \"center\">");
                        slotsDire.append("<td>");
                          slotsDire.append(new BigDecimal((rs1.getLong(18)/(rs.getInt(2)/60f))) .setScale(1, RoundingMode.HALF_UP).floatValue());
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                    slotsDire.append("</table>");
                  slotsDire.append("</td>");
                  slotsDire.append("<td>");
                    slotsDire.append(rs1.getInt(21) + "%");//% участия в убийствах команды
                  slotsDire.append("</td>");  
                  slotsDire.append("<td>");
                    slotsDire.append("<table width = 100%>");
                      slotsDire.append("<tr align = \"center\">");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(19));//золото за весь матч
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                      slotsDire.append("<tr align = \"center\">");
                        slotsDire.append("<td>");
                            slotsDire.append(rs1.getLong(20));//опыт за весь матч
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                    slotsDire.append("</table>");
                  slotsDire.append("</td>");  
                  slotsDire.append("<td>");
                    slotsDire.append("<table border = 1px, width = 100%>");//вещи в инвентаре героя (6штук)
                      slotsDire.append("<tr>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(8));
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(9));
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(10));
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                      slotsDire.append("<tr>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(11));
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(12));
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(13));
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                    slotsDire.append("</table>");
                  slotsDire.append("</td>"); 
                slotsDire.append("</tr>");
              slotsDire.append("</table>");
            slotsDire.append("</div");
            }
        }
        String caption = new String();
        StringBuilder page = new StringBuilder();
        if(rs.getBoolean(4))
          caption = "<td>Radiant <b><i>WIN</i></b></td><td>Dire<td></td>";
        else
          caption = "<td>Radiant></td><td>Dire <b><i>WIN</i></b<td></td>";
        PrintWriter out = response.getWriter();
        page.append("<!DOCTYPE html>");
        page.append("<html>");
          page.append("<head>");
            page.append("<meta charset=\"UTF-8\">");
            page.append("<style type = \"text/css\">");
              page.append(".layer1, .layer2 {background: #F2EFE6; border: 1px solid #B25538; padding: 10px; margin: 20px;}");
            page.append("</style>");
            page.append("<title>");
              page.append("Details for Match");
            page.append("</title>");
          page.append("</head>");
          page.append("<body bgcolor=\"#fdf5e6\">");
            page.append("<div class=\"layer1\">");
              page.append(matchOverView.toString());
            page.append("</div>");
            page.append("<div>");
              page.append("<table width = 800px, align = \"center\">");
                page.append("<tr  align = \"center\">");
                  page.append(caption);
                page.append("</tr>");
                page.append("<tr  align = \"center\">");
                  page.append("<td>");
                    page.append(slotsRadiant.toString());
                  page.append("</td>");
                  page.append("<td>");
                    page.append(slotsDire.toString());
                  page.append("</td>");
                page.append("</tr>");
              page.append("</table>");
            page.append("</div>");
            page.append("<div>");
              page.append("<table width = 800px, border = 2px, align = \"center\">");
                page.append("<tr align = \"center\", cols = \"4\">");
                  page.append("<td width = 25%>");
                    page.append(perkHero.toString());
                  page.append("</td>");
                  page.append("<td width = 25%>");
                    page.append(perkLast.toString());
                  page.append("</td>");
                  page.append("<td width = 25%>");
                    page.append(perkKill.toString());
                  page.append("</td>");
                  page.append("<td width = 25%>");
                    page.append(perkTower.toString());
                  page.append("</td>");
                page.append("</tr>");
              page.append("</table>");
            page.append("</div>");
          page.append("</body>");
        page.append("</html>");
        out.write(page.toString());
        try{
          statementMatch.close();
          statementSlot.close();
          rs.close();
          rs1.close();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }finally{
        try{
          connect.close();
        }catch(Exception ex){
          ex.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(request, response);
  }
  
}
