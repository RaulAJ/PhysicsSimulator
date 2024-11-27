package simulator.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableModel;
import java.awt.*;

public class InfoTable extends JPanel {
    String _title;
    TableModel _tableModel;
    InfoTable(String title, TableModel tableModel) {
        _title = title;
        _tableModel = tableModel;
        initGUI();
    }
    private void initGUI() {
// cambiar el layout del panel a BorderLayout()
        this.setLayout(new BorderLayout());
// añadir un borde con título al JPanel, con el texto _title
        Border border = BorderFactory.createTitledBorder(_title);
        this.setBorder(border);

        // añadir un JTable (con barra de desplazamiento vertical) que use
        // _tableModel
        JTable table = new JTable(_tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        this.add(scrollPane, BorderLayout.CENTER);
    }
}
