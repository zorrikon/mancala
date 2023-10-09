<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en">
<spring:url value="styles.css" var="css"/>
<link href="${css}" rel="stylesheet"/>

<body>
  <h1>Mancala</h1>

  <table class="mancala-board">
    <tr>

      <%-- Top player's goal --%>
      <td>
        <div class="goal">${pits[13]}</div>
      </td>

      <td>
        <table>

          <%-- Top player's pits --%>
          <tr class="${activePlayer=='Top' ? 'enabled' : 'disabled'}">
            <td>
              <%-- class="stones#" helps disable empty pits in the css --%>
              <a class="stones${pits[12]}" href="${pageContext.request.contextPath}/move/12">
                <div class="pit">${pits[12]}</div>
              </a>
            </td>
            <td>
              <a class="stones${pits[11]}" href="${pageContext.request.contextPath}/move/11">
                <div class="pit">${pits[11]}</div>
              </a>
            </td>
            <td>
              <a class="stones${pits[10]}" href="${pageContext.request.contextPath}/move/10">
                <div class="pit">${pits[10]}</div>
              </a>
            </td>
            <td>
              <a class="stones${pits[9]}" href="${pageContext.request.contextPath}/move/9">
                <div class="pit">${pits[9]}</div>
              </a>
            </td>
            <td>
              <a class="stones${pits[8]}" href="${pageContext.request.contextPath}/move/8">
                <div class="pit">${pits[8]}</div>
              </a>
            </td>
            <td>
              <a class="stones${pits[7]}" href="${pageContext.request.contextPath}/move/7">
                <div class="pit">${pits[7]}</div>
              </a>
            </td>
          </tr>

          <%-- Bottom player's pits --%>
          <tr class="${activePlayer=='Bottom' ? 'enabled' : 'disabled'}">
              <td>
                  <a class="stones${pits[0]}" href="${pageContext.request.contextPath}/move/0">
                      <div class="pit">${pits[0]}</div>
                  </a>
              </td>
              <td>
                  <a class="stones${pits[1]}" href="${pageContext.request.contextPath}/move/1">
                      <div class="pit">${pits[1]}</div>
                  </a>
              </td>
              <td>
                  <a class="stones${pits[2]}" href="${pageContext.request.contextPath}/move/2">
                      <div class="pit">${pits[2]}</div>
                  </a>
              </td>
              <td>
                  <a class="stones${pits[3]}" href="${pageContext.request.contextPath}/move/3">
                      <div class="pit">${pits[3]}</div>
                  </a>
              </td>
              <td>
                  <a class="stones${pits[4]}" href="${pageContext.request.contextPath}/move/4">
                      <div class="pit">${pits[4]}</div>
                  </a>
              </td>
              <td>
                  <a class="stones${pits[5]}" href="${pageContext.request.contextPath}/move/5">
                      <div class="pit">${pits[5]}</div>
                  </a>
              </td>
          </tr>
        </table>
      </td>

      <%-- Bottom player's goal --%>
      <td>
          <div class="goal">${pits[6]}</div>
      </td>
    </tr>
  </table>

  <p>${activePlayer} player's turn</p>

  <button onclick="location.href='${pageContext.request.contextPath}/bot-move'" type="buttom">
     Do Suggested Move
  </button>

  <h2 class="message">${message}</h2>

  <button type="reset" onclick="confirmRestartingGame()">Restart Game</button>

  <script>
    function confirmRestartingGame() {
      if (confirm("Are you sure you want to restart the game?")) {
        location.href='${pageContext.request.contextPath}/restart'
      }
    }
  </script>
</body>
</html>
