/**
 * Autor: Hugo Teixeira Mafra
 * Matricula: 201611540
 * Inicio: 12/02/2018
 * Ultima alteracao: 18/02/2018
 * Nome: Codificador de Sinal Digital
 * Funcao: simular o funcionamento do enlace fisico - independente do meio de transmissao -
 * atraves da implementacao de tres tipos de codificacoes estudadas no livro de Redes de Computadores, do Tanenbaum
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RepresentacaoGraficaMeioDeTransmissao extends JPanel {
    public static String[] bits; // Bits que irao ser representados como String
    public static String[] manchesterBits; // Bits que irao ser representados como String
    public static String[] manchesterDiferencialBits; // Bits que irao ser representados como String
    public static boolean clear; // Armazena "true" or "false" para limpar ou nao a tela
    public final int BINARIO = 1;
    public final int MANCHESTER = 2;
    public final int MANCHESTER_DIFERENCIAL = 3;
    /**
     * Este conjunto de variaveis finais definidas para organizar a posicao dos desenhos
     * feitos no painel
     */
    private final int LARGURA_LINHA_CLOCK = 30; // Define a largura da linha do clock
    private final int STREAM_Y = 30; // Linhas tracejadas no eixo vertical
    private final int STREAM_X = 135; // Linhas tracejadas no eixo horizontal
    private final int SINAL_POSITIVO_Y = 70; // Posicao do eixo vertical que ira ficar o sinal positivo
    private final int SINAL_NEGATIVO_Y = 120; // Posicao do eixo vertical que ira ficar o sinal negativo
    private final int SINAL_ZERO_Y = 100; // Posicao do eixo vertiral que ira indicar o meio entre os dois sinais
    private int tipoDeCodificacao = 0; // Armazena o tipo de codificacao
    private int STREAM_X2 = 0; // Plota a posicao dos bits em cima das linhas

    /**
     * Metodo: RepresentacaoGraficaMeioDeTransmissao
     * Funcao: construtor da classe. Define o background do painel como branco
     */
    public RepresentacaoGraficaMeioDeTransmissao() {
        super();
        setBackground(Color.WHITE);
    }

    public int getEncodingTechnique() {
        return this.tipoDeCodificacao;
    }

    public void setEncodingTechnique(int technique) {
        this.tipoDeCodificacao = technique;
    }

    /**
     * Metodo: paintComponent
     * Funcao: "override" do metodo da classe JComponent. Realiza desenhos graficos simples em um JPanel
     *
     * @param g determina regras e acoes para o desenho
     * @return void
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (clear)
            return;

        if (this.tipoDeCodificacao == 0)
            return;

        Graphics2D g2 = (Graphics2D) g;
        // Seta a qualidade de redenrizacao do desenho
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        desenhaLinhasDoClock(g2); // Desenha as linhas tracejadas do clock

        switch (this.tipoDeCodificacao) {
            case BINARIO:
                desenhaVoltPositivo(g2); // Desenha volt positivo
                desenhaVoltNegativo(g2); // Desenha volt negativo
                meioDeTransmissaoCodificacaoBinaria(g2); // Desenha linhas da codificacao binaria
                repaint();
                break;
            case MANCHESTER:
                desenhaVoltPositivo(g2);
                desenhaVoltNegativo(g2);
                meioDeTransmissaoCodificacaoManchester(g2);
                repaint();
                break;
            case MANCHESTER_DIFERENCIAL:
                desenhaVoltPositivo(g2);
                desenhaVoltNegativo(g2);
                meioDeTransmissaoCodificacaoManchesterDiferencial(g2);
                repaint();
                break;
            default:
                break;
        } // Fim do switch/case
    } // Fim do metodo paintComponent

    /**
     * Metodo: desenhaVoltPositivo
     * Funcao: desenha o volt positivo no painel.
     *
     * @param g2
     * @return void
     */
    public void desenhaVoltPositivo(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                30.0f, new float[]{8.0f}, 0.0f));
        g2.drawString("+0,85V", STREAM_X - 40, this.SINAL_POSITIVO_Y);
        g2.drawLine(STREAM_X, this.SINAL_POSITIVO_Y, STREAM_X2, this.SINAL_POSITIVO_Y);
    } // Fim do metodo desenhaVoltPositivo

    /**
     * Metodo: desenhaVoltNegativo
     * Funcao: desenha o volt negativo no painel
     *
     * @param g2
     * @return void
     */
    public void desenhaVoltNegativo(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                30.0f, new float[]{8.0f}, 0.0f));
        g2.drawString("-0,85V", STREAM_X - 40, this.SINAL_NEGATIVO_Y);
        g2.drawLine(STREAM_X, this.SINAL_NEGATIVO_Y, STREAM_X2, this.SINAL_NEGATIVO_Y);
    } // Fim do metodo desenhaVoltNegativo

    /**
     * Metodo: meioDeTransmissaoCodificacaoBinaria
     * Funcao: realiza o desenhos das linhas da codificacao binaria
     *
     * @param g2
     * @return void
     */
    public void meioDeTransmissaoCodificacaoBinaria(Graphics2D g2) {
        int z = this.STREAM_X;
        boolean flag = false;

        g2.setColor(Color.RED); // A linha sera da cor vermelha
        g2.setStroke(new BasicStroke(2.5f)); // Espessura da linha
        g2.setFont(new Font("Arial", Font.PLAIN, 15)); // Tipo e tamanho da fonte da palavra "Binaria
        g2.drawString("Binaria", 25, SINAL_ZERO_Y);

        StringBuilder sb = new StringBuilder();
        String s = "";

        for (int i = 0; i < bits.length; i++)
            s = sb.append(bits[i]).toString();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                // Desenha primeira linha
                g2.drawLine(z, this.SINAL_NEGATIVO_Y, z + this.LARGURA_LINHA_CLOCK, this.SINAL_NEGATIVO_Y);
                if (!flag) // Se flag == false
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z, this.SINAL_NEGATIVO_Y); // Mudanca de transicao
                flag = true;
            } else {
                // Desenha primeira linha
                g2.drawLine(z, this.SINAL_POSITIVO_Y, z + this.LARGURA_LINHA_CLOCK, this.SINAL_POSITIVO_Y);
                if (flag) // Se flag == true
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z, this.SINAL_POSITIVO_Y); // Mudanca de transicao
                flag = false;
            }
            z += this.LARGURA_LINHA_CLOCK; // Desloca as linhas para a direita
        }
    }

    /**
     * Metodo: meioDeTransmissaoCodificacaoManchester
     * Funcao:
     *
     * @param g2
     * @return void
     */
    public void meioDeTransmissaoCodificacaoManchester(Graphics2D g2) {
        int z = this.STREAM_X;

        boolean statusHigh = false;

        g2.setColor(Color.RED); // A linha sera da cor vermelha
        g2.setStroke(new BasicStroke(2.5f)); // Espessura da linha
        g2.setFont(new Font("Arial", Font.PLAIN, 15)); // Tipo e tamanho da fonte da palavra "Manchester"
        g2.drawString("Manchester", 10, SINAL_ZERO_Y); // Escreve a palavra "Manchester"

        StringBuilder sb = new StringBuilder();
        String s = "";

        for (int i = 0; i < manchesterBits.length; i++)
            s = sb.append(manchesterBits[i]).toString();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                if (statusHigh) {
                    // Desce ao nivel negativo
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z, this.SINAL_NEGATIVO_Y);
                    // Transicao do baixo para alto feita uma vez
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_NEGATIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z, this.SINAL_POSITIVO_Y);
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_POSITIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;
                    statusHigh = true;
                } else {
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_NEGATIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z, this.SINAL_POSITIVO_Y);
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_POSITIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;
                    statusHigh = true;

                } // Fim do else interno
            } else if (s.charAt(i) == '1') {
                if (statusHigh) {
                    // Transicao do alto para baixo
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_POSITIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;

                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z, this.SINAL_NEGATIVO_Y);
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_NEGATIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;
                    statusHigh = false;
                } else { // Vai para o nivel positivo
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z, this.SINAL_POSITIVO_Y);
                    // Vai do alto para baixo para indicar 0
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_POSITIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;

                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z, this.SINAL_NEGATIVO_Y);
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_NEGATIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;
                    statusHigh = false;

                } // Fim do else interno
            } // Fim do else if
        } // Fim do for
    } // Fim do metodo meioDeTransmissaoCodificacaoManchester

    /**
     * Metodo: meioDeTransmissaoCodificacaoManchesterDiferencial
     * Funcao:
     *
     * @param g2
     * @return void
     */
    public void meioDeTransmissaoCodificacaoManchesterDiferencial(Graphics2D g2) {
        int z = this.STREAM_X;

        boolean currentLevelHigh = true;

        g2.setColor(Color.RED); //  A linha sera da cor vermelha
        g2.setStroke(new BasicStroke(2.5f)); // Espessura da linha
        g2.setFont(new Font("Arial", Font.PLAIN, 15)); // Tipo e tamanho da fonte da palavra "Manchester Diferencial"
        g2.drawString("Manchester", 15, SINAL_ZERO_Y - 15); // Escreve a palavra "Manchester"
        g2.drawString("Diferencial", 15, SINAL_ZERO_Y + 5); // Escreve a palavra "Diferencial"

        StringBuilder sb = new StringBuilder();
        String s = "";

        for (int i = 0; i < manchesterDiferencialBits.length; i++)
            s = sb.append(manchesterDiferencialBits[i]).toString();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                if (currentLevelHigh) {
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z, this.SINAL_NEGATIVO_Y);
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_NEGATIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;

                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z, this.SINAL_POSITIVO_Y);
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_POSITIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;

                } else {
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z, this.SINAL_POSITIVO_Y);
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_POSITIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;

                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z, this.SINAL_NEGATIVO_Y);
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_NEGATIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;

                } // Fim do else interno
            } else {
                if (currentLevelHigh) {
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_POSITIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z, this.SINAL_NEGATIVO_Y);
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_NEGATIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;
                    currentLevelHigh = false;
                } else {
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_NEGATIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;
                    g2.drawLine(z, this.SINAL_NEGATIVO_Y, z, this.SINAL_POSITIVO_Y);
                    g2.drawLine(z, this.SINAL_POSITIVO_Y, z + this.LARGURA_LINHA_CLOCK / 2, this.SINAL_POSITIVO_Y);
                    z += this.LARGURA_LINHA_CLOCK / 2;
                    currentLevelHigh = true;
                } // Fim do else interno
            } // Fim do else
        } // Fim do for
    } // Fim do metodo meioDeTransmissaoCodificacaoManchesterDiferencial

    /**
     * Metodo: desenhaLinhasDoClock
     * Funcao: desenha as linhas tracejadas verticais e horizontais
     *
     * @return void
     */
    public void desenhaLinhasDoClock(Graphics2D g2) {
        g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                30.0f, new float[]{8.0f}, 0.0f));

        StringBuilder sb = new StringBuilder();
        String s1 = "";
        String s2 = "";
        String s3 = "";

        int x = this.STREAM_X;
        if (tipoDeCodificacao == BINARIO) {
            for (int i = 0; i < bits.length; i++)
                s1 = sb.append(bits[i]).toString();

            for (int i = 0; i <= s1.length(); i++) {
                g2.drawLine(x, 0, x, (int) this.getSize().getHeight());
                x += this.LARGURA_LINHA_CLOCK;
            }
        } else if (tipoDeCodificacao == MANCHESTER) {
            for (int i = 0; i < manchesterBits.length; i++)
                s2 = sb.append(manchesterBits[i]).toString();

            for (int i = 0; i <= s2.length(); i++) {
                g2.drawLine(x, 0, x, (int) this.getSize().getHeight());
                x += this.LARGURA_LINHA_CLOCK;
            }
        } else if (tipoDeCodificacao == MANCHESTER_DIFERENCIAL) {
            for (int i = 0; i < manchesterDiferencialBits.length; i++)
                s3 = sb.append(manchesterDiferencialBits[i]).toString();

            for (int i = 0; i <= s3.length(); i++) {
                g2.drawLine(x, 0, x, (int) this.getSize().getHeight());
                x += this.LARGURA_LINHA_CLOCK;
            }
        }

        this.STREAM_X2 = x - this.LARGURA_LINHA_CLOCK;
        if (x > (int) this.getSize().getWidth()) {
            int width = (int) this.getSize().getWidth() + (x - (int) this.getSize().getWidth());
            int height = (int) this.getSize().getHeight();
            this.setPreferredSize(new Dimension(width, height));
            this.revalidate();
        }


        if (tipoDeCodificacao == BINARIO) {
            x = this.STREAM_X + 10;
            for (int i = 0; i < s1.length(); i++) {
                g2.drawString(String.valueOf(s1.charAt(i)), x, this.STREAM_Y);
                x += this.LARGURA_LINHA_CLOCK;
            }
        } else if (tipoDeCodificacao == MANCHESTER) {
            x = this.STREAM_X + 10;
            for (int i = 0; i < s2.length(); i++) {
                g2.drawString(String.valueOf(s2.charAt(i)), x, this.STREAM_Y);
                x += this.LARGURA_LINHA_CLOCK;
            }
        } else if (tipoDeCodificacao == MANCHESTER_DIFERENCIAL) {
            x = this.STREAM_X + 10;
            for (int i = 0; i < s3.length(); i++) {
                g2.drawString(String.valueOf(s3.charAt(i)), x, this.STREAM_Y);
                x += this.LARGURA_LINHA_CLOCK;
            }
        }
    } // Fim do metodo desenhaLinhasDoClock
} // Fim do classe RepresentacaoGraficaMeioDeTransmissao