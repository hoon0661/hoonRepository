/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HangMan;

import Contoller.HangManController;
import Dao.HangManDao;
import Dao.HangManDaoImpl;
import Service.HangManExistingLetterException;
import Service.HangManService;
import Service.HangManServiceImpl;
import Service.HangManWrongLetterException;
import UI.HangManView;
import UI.IOConsoleImpl;
import UI.IOUser;



/**
 *
 * @author GKE
 */
public class App {
    public static void main(String args[]) throws HangManExistingLetterException, HangManWrongLetterException {
       IOUser io = new IOConsoleImpl();
       HangManDao dao = new HangManDaoImpl();
       HangManView view = new HangManView(io);
       HangManService service = new HangManServiceImpl(dao);
       HangManController controller = new HangManController(view, service);
       controller.run();
    }
}
