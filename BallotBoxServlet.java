package var.web.ws.poll;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/vote")
public class BallotBoxServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 3504733789801863681L;
	private final BallotBox election;

	public BallotBoxServlet() {
		super();
		this.election = BallotBox.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		final PrintWriter out = response.getWriter();
		out.println("<html><body>");
		final String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("vote")) {
				String alternative = request.getParameter("alternative");
				if ((alternative == null) || alternative.equals("")) {
					alternative = "ungültige Stimmenabgabe";
				}
				this.election.vote(alternative);
				out.println("<p>Ihre Stimme wurde gez&auml;hlt.</p>");
			} else if (action.equals("print")) {
				out.println("<h1>abgegebene Stimmen</h1>");
				out.println("<table border='1'>");
				out.println("<tr><th align='left'>Alternative</th><th align='right'>Stimmen</th></tr>");
				for (final String alternative : this.election.getChoices()) {
					out.println("<tr><th align='left'>" + alternative + "</th>");
					out.println("<td align='right'>" + this.election.getNumberOfVotes(alternative) + "</td></tr>");
				}
				out.println("<tr><th align='left'>Summe:</th>");
				out.println("<th align='right'>" + this.election.countVotes() + "</th></tr>");
				out.println("</table>");
			} else {
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,
						"action-Parameter hatte den Wert '" + action + "'. Erlaubte Werte sind 'vote' und 'print'");
			}
		} else {
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,
					"action-Parameter hatte keinen Wert gesetzt. Erlaubte Werte sind 'vote' und 'print'");
		}
		out.println("</body></html>");
		out.flush();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}