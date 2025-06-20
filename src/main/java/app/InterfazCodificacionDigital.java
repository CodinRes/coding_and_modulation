package app;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.awt.*;

/**
 * Interfaz gráfica para visualizar la codificación digital de una secuencia de bits.
 * Permite seleccionar el método de codificación y graficar la señal resultante.
 */
public class InterfazCodificacionDigital extends JFrame
{

    private JTextField campoBits;
    private JComboBox<String> comboMetodo;
    private JButton botonGraficar;
    private ChartPanel chartPanel;

    /**
     * Constructor de la interfaz.
     * Inicializa los componentes y configura la ventana principal.
     */
    public InterfazCodificacionDigital()
    {
        this.setTitle("Codificación Digital");
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Panel superior con controles
        JPanel panelControles = new JPanel(new GridLayout(2, 2, 10, 5));

        this.setCampoBits(new JTextField("1011001"));
        this.setComboMetodo(new JComboBox<>(new String[]{
                "NRZ-L", "NRZI", "Bipolar AMI", "Pseudoternario", "Manchester", "Manchester Diferencial"
        }));
        this.setBotonGraficar(new JButton("Graficar"));

        panelControles.add(new JLabel("Secuencia de bits:"));
        panelControles.add(this.getCampoBits());
        panelControles.add(new JLabel("Codificación:"));
        panelControles.add(this.getComboMetodo());

        add(panelControles, BorderLayout.NORTH);
        add(this.getBotonGraficar(), BorderLayout.SOUTH);

        // Panel para gráfico
        this.setChartPanel(new ChartPanel(null));
        add(this.getChartPanel(), BorderLayout.CENTER);

        this.getBotonGraficar().addActionListener(e -> graficar());

        this.setVisible(true);
    }

    /**
     * Genera y grafica la señal codificada según los parámetros seleccionados.
     */
    private void graficar()
    {
        String bits = this.getCampoBits().getText();
        String metodo = (String) this.getComboMetodo().getSelectedItem();
        int muestrasPorBit = 100;

        CodificadorDigital codificador = new CodificadorDigital(bits, muestrasPorBit);
        double[] senial;

        switch (metodo)
        {
            case "NRZ-L" -> senial = codificador.codificarNRZ_L();
            case "NRZI" -> senial = codificador.codificarNRZI();
            case "Bipolar AMI" -> senial = codificador.codificarBipolarAMI();
            case "Pseudoternario" -> senial = codificador.codificarPseudoternario();
            case "Manchester" -> senial = codificador.codificarManchester();
            case "Manchester Diferencial" -> senial = codificador.codificarManchesterDiferencial();
            default ->
            {
                JOptionPane.showMessageDialog(this, "Método no reconocido.");
                return;
            }
        }

        // Graficar
        XYSeries serie = new XYSeries("Señal codificada");
        double ts = 1.0 / muestrasPorBit;
        for (int i = 0; i < senial.length; i++)
        {
            serie.add(i * ts, senial[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(serie);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Codificación: " + metodo,
                "Tiempo (s)",
                "Nivel",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        this.getChartPanel().setChart(chart);
    }

    // Getters y Setters para los atributos privados

    /**
     * Devuelve el campo de texto para la secuencia de bits.
     */
    public JTextField getCampoBits() {
        return campoBits;
    }

    /**
     * Establece el campo de texto para la secuencia de bits.
     */
    public void setCampoBits(JTextField campoBits) {
        this.campoBits = campoBits;
    }

    /**
     * Devuelve el combo box para seleccionar el método de codificación.
     */
    public JComboBox<String> getComboMetodo() {
        return comboMetodo;
    }

    /**
     * Establece el combo box para seleccionar el método de codificación.
     */
    public void setComboMetodo(JComboBox<String> comboMetodo) {
        this.comboMetodo = comboMetodo;
    }

    /**
     * Devuelve el botón para graficar.
     */
    public JButton getBotonGraficar() {
        return botonGraficar;
    }

    /**
     * Establece el botón para graficar.
     */
    public void setBotonGraficar(JButton botonGraficar) {
        this.botonGraficar = botonGraficar;
    }

    /**
     * Devuelve el panel del gráfico.
     */
    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    /**
     * Establece el panel del gráfico.
     */
    public void setChartPanel(ChartPanel chartPanel) {
        this.chartPanel = chartPanel;
    }
}
