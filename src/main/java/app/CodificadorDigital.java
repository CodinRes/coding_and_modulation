package app;

import java.util.ArrayList;

/**
 * Clase que implementa varios esquemas de codificación digital:
 * NRZ-L, NRZI, Bipolar AMI, Pseudoternario, Manchester y Manchester Diferencial.
 */
public class CodificadorDigital
{

    private String bits;
    private int samplesPerBit;

    /**
     * Constructor.
     * 
     * @param bits Cadena binaria (ej. "101010")
     * @param samplesPerBit Cantidad de muestras por bit (resolución temporal)
     */
    public CodificadorDigital(String bits, int samplesPerBit)
    {
        setBits(bits);
        setSamplesPerBit(samplesPerBit);
    }

    // Getter y Setter para bits
    public String getBits() {
        return bits;
    }

    public void setBits(String bits) {
        this.bits = bits.replaceAll("[^01]", ""); // limpia caracteres no binarios
    }

    // Getter y Setter para samplesPerBit
    public int getSamplesPerBit() {
        return samplesPerBit;
    }

    public void setSamplesPerBit(int samplesPerBit) {
        this.samplesPerBit = samplesPerBit;
    }

    /** NRZ-L: 1 = +1, 0 = -1 */
    public double[] codificarNRZ_L()
    {
        ArrayList<Double> senial = new ArrayList<>();
        for (char bit : this.getBits().toCharArray())
        {
            double nivel = (bit == '1') ? 1.0 : -1.0;
            for (int i = 0; i < this.getSamplesPerBit(); i++)
            {
                senial.add(nivel);
            }
        }
        return toArray(senial);
    }

    /** NRZI: 1 = transición, 0 = mantiene nivel */
    public double[] codificarNRZI()
    {
        ArrayList<Double> senial = new ArrayList<>();
        double nivel = -1.0;
        for (char bit : this.getBits().toCharArray())
        {
            if (bit == '1')
            {
                nivel = -nivel;
            }
            for (int i = 0; i < this.getSamplesPerBit(); i++)
            {
                senial.add(nivel);
            }
        }
        return toArray(senial);
    }

    /** Bipolar AMI: 0 = 0, 1 = alterna entre +1 y -1 */
    public double[] codificarBipolarAMI()
    {
        ArrayList<Double> senial = new ArrayList<>();
        double nivel = 1.0;
        for (char bit : this.getBits().toCharArray())
        {
            if (bit == '0')
            {
                for (int i = 0; i < this.getSamplesPerBit(); i++)
                {
                    senial.add(0.0);
                }
            } else
            {
                for (int i = 0; i < this.getSamplesPerBit(); i++)
                {
                    senial.add(nivel);
                }
                nivel = -nivel; // alterna signo
            }
        }
        return toArray(senial);
    }

    /** Pseudoternario: 1 = 0, 0 = alterna entre +1 y -1 */
    public double[] codificarPseudoternario()
    {
        ArrayList<Double> senial = new ArrayList<>();
        double nivel = 1.0;
        for (char bit : this.getBits().toCharArray())
        {
            if (bit == '1')
            {
                for (int i = 0; i < this.getSamplesPerBit(); i++)
                {
                    senial.add(0.0);
                }
            } else
            {
                for (int i = 0; i < this.getSamplesPerBit(); i++)
                {
                    senial.add(nivel);
                }
                nivel = -nivel;
            }
        }
        return toArray(senial);
    }

    /** Manchester: 1 = -1 → +1, 0 = +1 → -1 */
    public double[] codificarManchester()
    {
        ArrayList<Double> senial = new ArrayList<>();
        for (char bit : this.getBits().toCharArray())
        {
            double primeraMitad = (bit == '1') ? -1.0 : 1.0;
            double segundaMitad = -primeraMitad;
            for (int i = 0; i < this.getSamplesPerBit() / 2; i++) senial.add(primeraMitad);
            for (int i = 0; i < this.getSamplesPerBit() / 2; i++) senial.add(segundaMitad);
        }
        return toArray(senial);
    }

    /** Manchester diferencial: 
     * 0 = transición al inicio, 
     * 1 = sin transición al inicio; 
     * siempre transición al medio del bit
     */
    public double[] codificarManchesterDiferencial()
    {
        ArrayList<Double> senial = new ArrayList<>();
        double nivel = 1.0; // nivel inicial

        for (char bit : this.getBits().toCharArray())
        {
            if (bit == '0')
            {
                nivel = -nivel; // transición al inicio
            }
            // mitad 1
            for (int i = 0; i < this.getSamplesPerBit() / 2; i++) senial.add(nivel);
            // transición al medio
            nivel = -nivel;
            // mitad 2
            for (int i = 0; i < this.getSamplesPerBit() / 2; i++) senial.add(nivel);
        }
        return toArray(senial);
    }

    /** Utilidad interna: convierte lista a arreglo */
    private double[] toArray(ArrayList<Double> lista)
    {
        double[] arr = new double[lista.size()];
        for (int i = 0; i < lista.size(); i++)
        {
            arr[i] = lista.get(i);
        }
        return arr;
    }
}
