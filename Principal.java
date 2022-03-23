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
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

// Classe principal
public class Principal extends JFrame {
    private int tipoDeEnquadramento;
    private boolean pausado = false;

    private final ButtonGroup optionButtonGroup;
    // Instancias de objetos do framework Swing
    private JPanel painelPrincipal = new JPanel();
    private JTextField textoEnviar = new JTextField(35);
    private JTextArea bitsReceptor = new JTextArea(1, 35);
    private JTextPane mensagemReceptor;
    private JButton iniciarButton = new JButton("Iniciar");
    private JButton limparButton = new JButton("Limpar");
    private JComboBox opcoesEnquadramento = new JComboBox();
    private final JScrollPane scroller2;
    private final DefaultStyledDocument doc;
    private JRadioButton binarioRadioButton = new JRadioButton("Binario");
    private JRadioButton manchesterRadioButton = new JRadioButton("Manchester");
    private JRadioButton manchesterDiferencialRadioButton = new JRadioButton("Manchester Diferencial");

    /* Instancia da classe RepresentacaoGraficaMeioDeTransmissao,
    ela ira realizar o desenho grafico das linhas de codificacao */
    private RepresentacaoGraficaMeioDeTransmissao camadaFisica = new RepresentacaoGraficaMeioDeTransmissao();

    /**
     * Metodo: Principal
     * Funcao: construtor da classe Principal. Organiza e Instancia a parte grafica do projeto
     */
    public Principal() {
        painelPrincipal.setLayout(new GridBagLayout()); // Seta o layout do JPanel como GridBagLayout

        addItem(painelPrincipal, new JLabel("Letras a enviar:"), 0, 0, 1, 1, GridBagConstraints.WEST);
        addItem(painelPrincipal, new JLabel("Bits enviados:"), 0, 2, 1, 1, GridBagConstraints.WEST);
        addItem(painelPrincipal, new JLabel("Mensagem recebida:"), 0, 3, 1, 1, GridBagConstraints.WEST);

        addItem(painelPrincipal, textoEnviar, 1, 0, 1, 1, GridBagConstraints.WEST);

        Border border1 = BorderFactory.createLineBorder(Color.BLACK);
        bitsReceptor.setBorder(BorderFactory.createCompoundBorder(border1,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        bitsReceptor.setEditable(false);
        JScrollPane scroller1 = new JScrollPane(bitsReceptor); // Adiciona um "scroller" a area de texto "bitsReceptor"
        scroller1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // Ira aparecer caso necessario
        addItem(painelPrincipal, scroller1, 1, 2, 1, 1, GridBagConstraints.WEST);


        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(iniciarButton);
        buttonBox.add(Box.createHorizontalStrut(20));
        buttonBox.add(limparButton);
        addItem(painelPrincipal, buttonBox, 1, 1, 1, 1, GridBagConstraints.WEST);
        iniciarButton.addActionListener(new ActionListener() { // Acao do botao "Iniciar"
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicacaoTransmissora(e);
            }
        });
        limparButton.addActionListener(new ActionListener() { // Acao do botao "Limpar"
            @Override
            public void actionPerformed(ActionEvent e) {
                limpaTela(e);
            }
        });

        Border border2 = BorderFactory.createLineBorder(Color.BLACK);
        doc = new DefaultStyledDocument();
        mensagemReceptor = new JTextPane(doc);
        mensagemReceptor.setPreferredSize(new Dimension(410, 50));
        mensagemReceptor.setBorder(BorderFactory.createCompoundBorder(border2,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        mensagemReceptor.setEditable(false);
        addItem(painelPrincipal, mensagemReceptor, 1, 3, 1, 1, GridBagConstraints.WEST);

        opcoesEnquadramento.setBackground(SystemColor.control);
        opcoesEnquadramento.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                escolherTipoDeEnquadramento(e);
            }
        });
        addItem(painelPrincipal, opcoesEnquadramento, 3, 0, 0, 0, GridBagConstraints.NORTH);
        opcoesEnquadramento.addItem("Contagem de caracteres");
        opcoesEnquadramento.addItem("Insercao de bytes ou caracteres");
        opcoesEnquadramento.addItem("Insercao de bits");
        opcoesEnquadramento.addItem("Violacoes de codificacao da camada fisica");


        optionButtonGroup = new ButtonGroup();
        optionButtonGroup.add(binarioRadioButton);
        optionButtonGroup.add(manchesterRadioButton);
        optionButtonGroup.add(manchesterDiferencialRadioButton);
        Box choiceBox = Box.createVerticalBox();
        choiceBox.add(binarioRadioButton);
        choiceBox.add(manchesterRadioButton);
        choiceBox.add(manchesterDiferencialRadioButton);
        choiceBox.setBorder(BorderFactory.createTitledBorder("Tipo de Codificacao"));
        binarioRadioButton.setSelected(true); // A opcao "Binario" ira ser selecionada automaticamente
        binarioRadioButton.setBackground(SystemColor.control);
        manchesterRadioButton.setBackground(SystemColor.control);
        manchesterDiferencialRadioButton.setBackground(SystemColor.control);
        addItem(painelPrincipal, choiceBox, 4, 2, 0, 0, GridBagConstraints.NORTH);

        scroller2 = new JScrollPane(camadaFisica);
        scroller2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller2.setPreferredSize(new Dimension(600, 200));
        Box drawBox = Box.createHorizontalBox();
        drawBox.add(scroller2);
        drawBox.setBorder(BorderFactory.createTitledBorder("Desenho da Codificacao"));
        addItem(painelPrincipal, drawBox, 0, 5, 0, 0, GridBagConstraints.WEST);

        this.add(painelPrincipal);
        this.setTitle("Codificador de Sinal Digital"); // Define o titulo
        this.setLocationByPlatform(true); // Define se a janela deve aparecer na localizacao padrao do Sistema Operacional
        this.pack(); // A janela sera ajustada de acordo com o numero de subcomponentes
        this.setResizable(false); // Nao permite expandir a janela
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Finaliza o programa ao fechar a janela
    }

    // Metodo main
    public static void main(String[] args) {
        // Cria o quadro na thread de despacho de evento
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Principal principal = new Principal(); // Instancia da classe Principal
                principal.setVisible(true); // Seta o JFrame como visivel
            }
        });
    } // Fim do metodo main

    /**
     * Metodo: aplicacaoTransmissora
     * Funcao: armazenar o texto digitado em uma string e envia-lo para o metodo "camadaAplicacaoTransmissora()"
     *
     * @param e realiza o evento
     * @return void
     */
    private void aplicacaoTransmissora(ActionEvent e) {
        String mensagem = textoEnviar.getText(); // Armazena a texto digitado na String mensagem
        camadaAplicacaoTransmissora(e, mensagem); // Chama a proxima camada
    } // Fim do metodo aplicacaoTransmissora

    /**
     * Metodo: camadaAplicacaoTransmissora
     * Funcao: guarda os codigos ASCII em um array de bytes e o envia para o metodo "camadaFisicaTransmissora()"
     *
     * @param e
     * @param mensagem
     * @return void
     */
    private void camadaAplicacaoTransmissora(ActionEvent e, String mensagem) {
        byte[] quadroByte = mensagem.getBytes(); // Insere um codigo ASCII em cada posicao do vetor

        System.out.println("Tamanho do quadroByte: " + quadroByte.length);
        int[] quadroInteger = new int[quadroByte.length];

        // convertendo byteArray para intArray
        for (int i = 0; i < quadroByte.length; quadroInteger[i] = quadroByte[i++]) ;

        System.out.print("\nTamanho do quadroInteger: " + quadroInteger.length + "\n");
        System.out.print(Arrays.toString(quadroInteger));

        if (textoEnviar.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "A caixa de texto esta vazia", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            camadaEnlaceDadosTransmissora(e, quadroInteger);
        }
    } // Fim do metodo camadaAplicacaoTransmissora

    /**
     * Metodo: limpaTela
     * Funcao: limpa o painel e os campos de texto exibidos na aplicacao
     *
     * @param e realiza a acao
     * @return void
     */
    private void limpaTela(ActionEvent e) {
        camadaFisica.clear = true; // Faz limpar a tela
        bitsReceptor.setText("");
        mensagemReceptor.setText("");
        revalidate(); // Revalida a tela
        repaint(); // Repinta a tela
    } // Fim do metodo limpaTela

    private void escolherTipoDeEnquadramento(ItemEvent e) {
        System.out.print("\nEscolher tipo de enquadramento\n");
        tipoDeEnquadramento = opcoesEnquadramento.getSelectedIndex();
        System.out.println("Tipo de enquadramento: " + tipoDeEnquadramento);
    }

    private void camadaEnlaceDadosTransmissora(ActionEvent e, int[] quadro) {
        System.out.print("\nCamada Enlace de Dados Transmissora\n");
        int[] quadroEnquadrado = camadaEnlaceDadosTransmissoraEnquadramento(quadro);

        System.out.println("Imprimindo quadro enquadrado: " + Arrays.toString(quadroEnquadrado));

        sendFrames(e, quadroEnquadrado);

    }

    private void sendFrames(ActionEvent e, int[] quadroEnquadrado) {
        switch (this.tipoDeEnquadramento) {
            case 0:
                int x = 0;
                int[] quadro;
                while (x < quadroEnquadrado.length) {
                    int cont = 0;
                    int index = quadroEnquadrado[x];
                    quadro = new int[index];
                    quadro[cont] = index;

                    cont++;
                    x++;
                    for (int y = 0; y < index - 1; y++) {
                        quadro[cont] = quadroEnquadrado[x];
                        cont++;
                        x++;
                    }

                    camadaFisicaTransmissora(e, quadroEnquadrado);
                }
                break;
            case 1:
                x = 0;
                int aux = 0;

                Vector<Integer> vector = new Vector<>();

                while (aux < quadroEnquadrado.length) {
                    int cont = 0;
                    if (quadroEnquadrado[x] == 33) {
                        vector.add(quadroEnquadrado[x]);
                        x++;
                        aux++;
                    }

                    if (vector.size() < quadroEnquadrado.length) {
                        vector.add(quadroEnquadrado[x]);
                    } else {

                    }

                    while (quadroEnquadrado[x] != 33 && vector.size() < quadroEnquadrado.length) {
                        x++;
                        vector.add(quadroEnquadrado[x]);
                    }

                    quadro = new int[vector.size()];
                    for (int y = 0; y < quadro.length; y++) {
                        quadro[cont] = vector.firstElement();
                        vector.remove(0);
                        cont++;
                        aux++;
                    }

                    x++;
                    camadaFisicaTransmissora(e, quadroEnquadrado);
                }
                break;
            default:
                break;
        }

    }

    private int[] camadaEnlaceDadosTransmissoraEnquadramento(int[] quadro) {
        int[] quadroEnquadrado = new int[0];
        switch (this.tipoDeEnquadramento) {
            case 0:
                quadroEnquadrado = camadaDeEnlaceTransmissoraEnquadramentoContagemDeCaracteres(quadro);
                break;
            case 1:
                quadroEnquadrado = camadaDeEnlaceTransmissoraEnquadramentoInsercaoDeCaracteres(quadro);
                break;
            case 2:
                // camadaDeEnlaceTransmissoraEnquadramentoInsercaoDeBits(quadro);
                break;
            case 3:
                // camadaDeEnlaceTransmissoraEnquadramentoViolacaoCamadaFisica(quadro);
                break;
            default:
                break;
        }

        return quadroEnquadrado;
    }

    private int[] camadaDeEnlaceTransmissoraEnquadramentoContagemDeCaracteres(int[] quadro) {
        int[] quadroEnquadrado;
        int cont = 0;

        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int x = quadro.length - 1; x >= 0; x--) {
            linkedList.addFirst(quadro[x]);
            cont++;

            if (quadro[x] == 32 || x == 0) {
                linkedList.addFirst(cont + 1);
                cont = 0;
            }
        }

        quadroEnquadrado = new int[linkedList.size()];
        for (int x = 0; x < quadroEnquadrado.length; x++) {
            quadroEnquadrado[x] = linkedList.getFirst();
            linkedList.removeFirst();
        }

        return quadroEnquadrado;
    }

    private int[] camadaDeEnlaceTransmissoraEnquadramentoInsercaoDeCaracteres(int[] quadro) {
        int[] quadroEnquadrado;
        char flag = '!';
        char escapeCharacter = '.';

        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int x = quadro.length - 1; x >= 0; x--) {
            if (x == 0) {
                linkedList.addFirst(quadro[x]);
                linkedList.addFirst((int) flag);
            } else if ((quadro.length - 1) == x) {
                linkedList.addFirst((int) flag);
                linkedList.addFirst(quadro[x]);
            } else if (quadro[x] == 32) { // if == espaco
                linkedList.addFirst(quadro[x]);
                linkedList.addFirst((int) flag);
                linkedList.addFirst((int) flag);
            } else if (quadro[x] == 46) { // if == '.'
                linkedList.addFirst((int) escapeCharacter);
            } else {
                linkedList.addFirst(quadro[x]);
            }

        }

        quadroEnquadrado = new int[linkedList.size()];
        for (int x = 0; x < quadroEnquadrado.length; x++) {
            quadroEnquadrado[x] = linkedList.getFirst();
            linkedList.removeFirst();
        }

        return quadroEnquadrado;
    }

    private int[] camadaDeEnlaceTransmissoraEnquadramentoInsercaoDeBits(int[] quadro) {
        return new int[0];
    }

    private int[] camadaDeEnlaceTransmissoraEnquadramentoViolacaoCamadaFisica(int[] quadro) {
        return new int[0];
    }

    private void camadaFisicaTransmissora(ActionEvent e, int[] quadro) {
        System.out.print("Camada Fisica Transmissora");
        camadaFisica.clear = false; // Impede de limpar a tela
        int[] fluxoBrutoDeBits = new int[0];

        if (binarioRadioButton.isSelected()) {
            try {
                camadaFisica.revalidate();
                camadaFisica.repaint();

                limpaTela(e);
                camadaFisica.clear = false;

                fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoBinaria(quadro);

                camadaFisica.bits = new String[fluxoBrutoDeBits.length];
                for (int i = 0; i < fluxoBrutoDeBits.length; i++)
                    camadaFisica.bits[i] = Integer.toBinaryString(fluxoBrutoDeBits[i]);

                camadaFisica.setEncodingTechnique(camadaFisica.BINARIO);

                for (String s : camadaFisica.bits)
                    bitsReceptor.append(s);

                System.out.println("Imprimindo bits receptor: " + Arrays.toString(camadaFisica.bits));
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(this, "Entrada Invalida", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this,
                        "Excecao Desconhecida Ocorreu",
                        "Exception",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (manchesterRadioButton.isSelected()) {
            try {
                camadaFisica.revalidate();
                camadaFisica.repaint();

                limpaTela(e);
                camadaFisica.clear = false;

                fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchester(quadro);


                camadaFisica.manchesterBits = new String[fluxoBrutoDeBits.length];
                for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
                    camadaFisica.manchesterBits[i] = Integer.toBinaryString(fluxoBrutoDeBits[i]);
                }


                camadaFisica.setEncodingTechnique(camadaFisica.MANCHESTER);

                for (String s : camadaFisica.manchesterBits)
                    bitsReceptor.append(s);

            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(this, "Entrada Invalida", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this,
                        "Excecao Desconhecida Ocorreu",
                        "Exception",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (manchesterDiferencialRadioButton.isSelected()) {
            try {
                camadaFisica.revalidate();
                camadaFisica.repaint();

                limpaTela(e);
                camadaFisica.clear = false;

                fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);

                camadaFisica.manchesterDiferencialBits = new String[fluxoBrutoDeBits.length];
                for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
                    camadaFisica.manchesterDiferencialBits[i] = Integer.toBinaryString(fluxoBrutoDeBits[i]);
                }

                camadaFisica.setEncodingTechnique(camadaFisica.MANCHESTER_DIFERENCIAL);
                camadaFisica.repaint();

                for (String s : camadaFisica.manchesterDiferencialBits)
                    bitsReceptor.append(s);
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(this, "Entrada Invalida", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this,
                        "Excecao Desconhecida Ocorreu",
                        "Exception",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        meioDeComunicacao(fluxoBrutoDeBits);
    }

    /**
     * Metodo: meioDeTransmissaoCodificacaoBinaria
     * Funcao: Codifica um vetor de bytes contendo os codigos ASCII de caracteres alfabeticos
     * para a codificacao Binaria e insere em um array de inteiros em forma de bits
     *
     * @param quadro = array de bytes contendo codigos ASCII de cada caracter alfabetico
     * @return int[]
     */
    public static int[] camadaFisicaTransmissoraCodificacaoBinaria(int[] quadro) {
        System.out.print("\nCamada Fisica Transmissora Codificacao Binaria\n");
        System.out.print("Imprimindo quadro: " + Arrays.toString(quadro) + "\n");

        // Variavel que ira receber o comprimento (length) do array "quadro" dividido por quatro
        int n = quadro.length / 4;

        // Verifica o comprimento do quadro divido por quatro tiver resto diferente de 0
        if (quadro.length % 4 != 0)
            n++;

        int[] bits = new int[n]; // Array que ira conter os inteiros com os bits armazenados no array "quadro"

        int index = 0; // Variavel auxiliar
        // Realiza loop ate o i ser menor que o comprimento (length) do vetor bits
        for (int i = 0; i < bits.length; i++) {
            bits[i] = quadro[index]; // bits[0] = quadro[0]
            index++;

            for (int y = 0; y < 3; y++) {
                if (index < quadro.length) {
                    bits[i] = bits[i] << 8; // Desloca 8 bits a esquerda
                    bits[i] = bits[i] | quadro[index]; // Concatena os bits na posicao "i" com os bits do quadro na posicao "aux"
                    index++;
                } // fim do if
            } // fim do for
        } // Fim do for

        return bits;
    } // Fim do metodo camadaFisicaTransmissoraCodificacaoBinaria

    /**
     * Metodo: camadaFisicaTransmissoraCodificacaoManchester
     * Funcao: codifica um vetor de bytes contendo os codigos ASCII de caracteres alfabeticos
     * para a codificacao Manchester e insere em um array de inteiros em forma de bits
     *
     * @param quadro
     * @return int[]
     */
    private int[] camadaFisicaTransmissoraCodificacaoManchester(int[] quadro) {
        System.out.print("\nCamada Fisica Transmissora Codificacao Manchester\n");

        // Variavel que ira receber o comprimento (length) do array "quadro" dividido por dois
        int n = quadro.length / 2;

        // Verifica o comprimento do quadro divido por quatro tiver resto diferente de 0
        if (quadro.length % 2 != 0) {
            n++;
        }

        // Array que ira conter os inteiros com os bits armazenados no array "quadro"
        int[] bits = new int[n];

        int aux = 0; // Variavel auxiliar
        for (int x = 0; x < bits.length; x++) {
            for (int y = 0; y < 2; y++) {
                if (aux < quadro.length) {
                    int temp = quadro[aux];
                    for (int i = 7; i >= 0; i--) {
                        bits[x] = bits[x] << 1;
                        int mask = 1 << i; // Desloca 7 bits a esquerda ate 0
                        if (Integer.toBinaryString(temp & mask).charAt(0) == '0') {
                            bits[x] = bits[x] | 1; // Adiciona o bit 1
                        } else if (Integer.toBinaryString(temp & mask).charAt(0) == '1') {
                            bits[x] = bits[x] | 0; // Adiciona o bit 0
                        }
                        int t = Integer.parseInt(String.valueOf(Integer.toBinaryString(temp & mask).charAt(0)));
                        bits[x] = bits[x] << 1;
                        bits[x] = bits[x] | t;
                    }
                    aux++;
                }
            }
        }

        return bits;
    } // Fim do metodo camadaFisicaTransmissoraCodificacaoManchester

    /**
     * Metodo: camadaFisicaTransmissoraCodificacaoManchesterDiferencial
     * Funcao: Codifica um vetor de bytes contendo os codigos ASCII de caracteres alfabeticos
     * para a codificacao Manchester Diferencial e insere em um array de inteiros em forma de bits
     *
     * @param quadro
     * @return int[]
     */
    private int[] camadaFisicaTransmissoraCodificacaoManchesterDiferencial(int[] quadro) {
        System.out.println("\nCamada Fisica Transmissora Codificacao Manchester Diferencial\n");

        // Variavel que ira receber o comprimento (length) do array "quadro" dividido por dois
        int n = quadro.length / 2;

        // Verifica o comprimento do quadro divido por quatro tiver resto diferente de 0
        if (quadro.length % 2 != 0) {
            n++;
        }

        // Array que ira conter os inteiros com os bits armazenados no array "quadro"
        int[] bits = new int[n];

        int aux = 0; // Variavel auxiliar
        for (int x = 0; x < bits.length; x++) {
            for (int y = 0; y < 2; y++) {
                if (aux < quadro.length) {
                    int temp = quadro[aux];

                    for (int i = 7; i >= 0; i--) {
                        bits[x] = bits[x] << 1;
                        int mask = 1 << i;
                        if (Integer.toBinaryString(temp & mask).charAt(0) == '0') {
                            bits[x] = bits[x] | 1;
                        } else if (Integer.toBinaryString(temp & mask).charAt(0) == '1') {
                            bits[x] = bits[x] | 0;
                        }
                        int t = Integer.parseInt(String.valueOf(Integer.toBinaryString(temp & mask).charAt(0)));
                        bits[x] = bits[x] << 1;
                        bits[x] = bits[x] | t;
                    }
                    aux++;
                } // fim do if
            } // fim do for
        } // fim do for

        return bits;
    } // Fim do metodo camadaFisicaTransmissoraCodificacaoManchesterDiferencial

    /**
     * Metodo: meioDeComunicacao
     * Funcao: simular a transmissao de comunicacao da informacao no meio de comunicacao
     * passando de uma variavel para outra
     *
     * @param fluxoBrutoDeBits eh o fluxo de binarios codificados
     * @return void
     */
    public void meioDeComunicacao(int[] fluxoBrutoDeBits) {
        System.out.print("\nMeio de Comunicação\n");

        int[] fluxoBrutoDeBitsPontoA = new int[fluxoBrutoDeBits.length];

        for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
            fluxoBrutoDeBitsPontoA[i] = fluxoBrutoDeBits[i];
        }

        int[] fluxoBrutoDeBitsPontoB = new int[fluxoBrutoDeBits.length];

        for (int i = 0; i < fluxoBrutoDeBitsPontoA.length; i++) {
            // fluxoBrutoDeBitsPontoB[i] = (fluxoBrutoDeBitsPontoA[i] and 0000 0000) or (fluxoBrutoDeBitsPontoA[i] and 1111 1111)
            fluxoBrutoDeBitsPontoB[i] = (fluxoBrutoDeBitsPontoA[i] & ~0xff) | (fluxoBrutoDeBitsPontoA[i] & 0xff);
//            fluxoBrutoDeBitsPontoB[i] = (fluxoBrutoDeBitsPontoA[i] & 0xfffffff0) | (fluxoBrutoDeBitsPontoA[i] & 0xf);
        }

        camadaFisicaReceptora(fluxoBrutoDeBitsPontoB);
    } // Fim do metodo meioDeComunicacao

    /**
     * Metodo: camadaFisicaReceptora
     * Funcao: recebe os bits do vetor de inteiros e decodifica de acordo
     * com o tipo de codificacao
     *
     * @param fluxoBrutoDeBits
     * @return void
     */
    private void camadaFisicaReceptora(int[] fluxoBrutoDeBits) {
        System.out.print("\nCamada Fisica Receptora\n");
        final int BINARY = 1, MANCHESTER = 2, MANCHESTER_DIFERENCIAL = 3;

        int[] quadro = new int[0];
        switch (camadaFisica.getEncodingTechnique()) {
            case BINARY:
                quadro = camadaFisicaReceptoraDecodificacaoBinaria(fluxoBrutoDeBits);
                break;
            case MANCHESTER:
                quadro = camadaFisicaReceptoraDecodificacaoManchester(fluxoBrutoDeBits);
                break;
            case MANCHESTER_DIFERENCIAL:
                quadro = camadaFisicaReceptoraDecodificacaoManchesterDiferencial(fluxoBrutoDeBits);
                break;
            default:
                break;
        } // Fim do switch/case

        camadaEnlaceDadosReceptora(quadro); // chama a proxima camada
    } // Fim do metodo camadaFisicaReceptora

    /**
     * Metodo: camadaFisicaReceptoraDecodificacaoBinaria
     * Funcao:
     *
     * @param fluxoBrutoDeBits
     * @return int[]
     */
    private int[] camadaFisicaReceptoraDecodificacaoBinaria(int[] fluxoBrutoDeBits) {
        System.out.print("\nCamada Fisica Receptora Decodificacao Binaria\n");
        int[] quadro = new int[fluxoBrutoDeBits.length * 4];

        for (int x = 0; x < quadro.length; x++) {
            quadro[x] = -1;
        } // fim do for

        int i = 31;
        if (fluxoBrutoDeBits[fluxoBrutoDeBits.length - 1] <= 255) {
            i = 7;
        } else if (fluxoBrutoDeBits[fluxoBrutoDeBits.length - 1] <= 65535) {
            i = 15;
        } else if (fluxoBrutoDeBits[fluxoBrutoDeBits.length - 1] <= 16777215) {
            i = 23;
        } // fim do if

        int cont2 = 0;
        for (int x = 0; x < fluxoBrutoDeBits.length; x++) {
            int y = 31;
            if (x == fluxoBrutoDeBits.length - 1) {
                y = i;
            } // fim do if
            String imp = "";
            int cont1 = 1;

            for (; y >= 0; y--) {
                int mask = 1 << y;
                imp += "" + Integer.toBinaryString(fluxoBrutoDeBits[x] & mask).charAt(0);
                if (cont1 != 0 && cont1 % 8 == 0) {
                    quadro[cont2] = Integer.parseInt(imp, 2);
                    cont2++;
                    imp = "";
                } // fim do if
                cont1++;
            } // fim do for
        } // fim do for

        return quadro;
    } // Fim do metodo camadaFisicaReceptoraDecodificacaoBinaria

    /**
     * Metodo: camadaFisicaReceptoraDecoficacaoManchester
     * Funcao:
     *
     * @param fluxoBrutoDeBits
     * @return int[]
     */
    public int[] camadaFisicaReceptoraDecodificacaoManchester(int[] fluxoBrutoDeBits) {
        System.out.print("\nCamada Fisica Receptora Decodificacao Manchester\n");
        int[] quadro = new int[fluxoBrutoDeBits.length * 2];

        for (int x = 0; x < quadro.length; x++)
            quadro[x] = -1;

        int i = 31;

        String n = "" + fluxoBrutoDeBits[fluxoBrutoDeBits.length - 1];
        if (n.length() <= 6) {
            i = 15;
        }

        int aux2 = 0;
        for (int x = 0; x < fluxoBrutoDeBits.length; x++) {
            int y = 31;
            if (x == fluxoBrutoDeBits.length - 1) {
                y = i;
            }
            String imp = "";
            int aux1 = 1;

            while (y >= 0) {
                int mask = 1 << y;

                if ((aux1 - 1 != 0) && (aux1 - 1) % 2 != 0) {
                    imp += "" + Integer.toBinaryString(fluxoBrutoDeBits[x] & mask).charAt(0);
                }

                if (aux1 != 0 && aux1 % 16 == 0) {
                    quadro[aux2] = Integer.parseInt(imp, 2);
                    aux2++;
                    imp = "";
                }
                aux1++;
                y--;
            }
        }
        return quadro;
    } // Fim do metodo camadaFisicaReceptoraDecodificacaoManchester

    /**
     * Metodo: camadaFisicaReceptoraDecodificacaoManchesterDiferencial
     * Funcao:
     *
     * @param fluxoBrutoDeBits
     * @return int[]
     */
    private int[] camadaFisicaReceptoraDecodificacaoManchesterDiferencial(int[] fluxoBrutoDeBits) {
        System.out.print("\nCamada Fisica Receptora Decodificacao Manchester Diferencial\n");
        int[] quadro = new int[fluxoBrutoDeBits.length * 2];

        for (int x = 0; x < quadro.length; x++)
            quadro[x] = -1;

        int i = 31;

        String n = "" + fluxoBrutoDeBits[fluxoBrutoDeBits.length - 1];
        if (n.length() <= 6) {
            i = 15;
        }

        int aux2 = 0;
        for (int x = 0; x < fluxoBrutoDeBits.length; x++) {
            int y = 31;
            if (x == fluxoBrutoDeBits.length - 1) {
                y = i;
            }
            String imp = "";
            int aux1 = 1;

            while (y >= 0) {
                int mask = 1 << y;

                if ((aux1 - 1 != 0) && (aux1 - 1) % 2 != 0) {
                    imp += "" + Integer.toBinaryString(fluxoBrutoDeBits[x] & mask).charAt(0);
                }

                if (aux1 != 0 && aux1 % 16 == 0) {
                    quadro[aux2] = Integer.parseInt(imp, 2);
                    aux2++;
                    imp = "";
                }
                aux1++;
                y--;
            }
        }
        return quadro;
    }

    private void camadaEnlaceDadosReceptora(int[] quadro) {
        int[] quadroDesenquadrado = camadaEnlaceDadosReceptoraEnquadramento(quadro);

        System.out.println("Imprimindo quadro desenquadrado: " + Arrays.toString(quadroDesenquadrado));

        camadaDeAplicacaoReceptora(quadroDesenquadrado);
    }

    private int[] camadaEnlaceDadosReceptoraEnquadramento(int[] quadro) {
        int[] quadroDesenquadrado = new int[0];
        switch (this.tipoDeEnquadramento) {
            case 0:
                quadroDesenquadrado = camadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres(quadro);
                break;
            case 1:
                quadroDesenquadrado = camadaEnlaceDadosReceptoraEnquadramentoInsercaoDeCaracteres(quadro);
                break;
            case 2:
                break;
            default:
                break;
        }

        return quadroDesenquadrado;
    }

    /**
     * Metodo: camadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres
     * Funcao:
     *
     * @param quadro
     * @return
     */
    private int[] camadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres(int[] quadro) {
        System.out.print("\nCamada Enlace Dados Receptora Enquadramento Contagem De Caracteres\n");
        int[] quadroDesenquadrado;
        int aux = 0;

        Vector<Integer> vector = new Vector<>();
        while (aux < quadro.length) {
            int letras = 0;
            int indexAux = quadro[aux] - 1;
            aux++;
            while (letras < indexAux) {
                vector.add(quadro[aux]);
                letras++;
                aux++;
            }
        }

        quadroDesenquadrado = new int[vector.size()];
        for (int x = 0; x < quadroDesenquadrado.length; x++) {
            quadroDesenquadrado[x] = vector.firstElement();
            vector.remove(0);
        }

        return quadroDesenquadrado;
    }

    /**
     * Metodo: camadaEnlaceDadosReceptoraEnquadramentoInsercaoDeCaracteres
     * Funcao:
     *
     * @param quadro
     * @return
     */
    public int[] camadaEnlaceDadosReceptoraEnquadramentoInsercaoDeCaracteres(int[] quadro) {
        System.out.print("\nCamada Enlace Dados Receptora Enquadramento Insercao De Caracteres\n");

        int[] quadroDesenquadrado;
        int x = 0;

        Vector<Integer> vector = new Vector<>();
        while (x < quadro.length) {
            if (quadro[x] == 33 || x == 0) {
                if (quadro[x] == 33) {
                    x++;
                } else if (quadro[x] != -1) {
                    vector.add(quadro[x]);
                    x++;
                } else {
                    x++;
                }
            } else if (quadro[x] != 33) {
                if (quadro[x] == 33) {
                    x++;
                } else if (quadro[x] != -1) {
                    vector.add(quadro[x]);
                    x++;
                } else {
                    x++;
                }
            } else if (quadro[x] == -1) {
                x++;
            }
        }

        quadroDesenquadrado = new int[vector.size()];
        for (int y = 0; y < quadroDesenquadrado.length; y++) {
            quadroDesenquadrado[y] = vector.firstElement();
            vector.remove(0);
        }

        System.out.println("\ndsgsdgsdgsdgsdgsgsgs: " + Arrays.toString(quadroDesenquadrado));

        return quadroDesenquadrado;
    }

    public int[] CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBits(int quadro[]) {
        return new int[0];
    }

    public int[] CamadaEnlaceDadosReceptoraEnquadramentoViolacaoDaCamadaFisica(int quadro[]) {
        return new int[0];
    }

    /**
     * Metodo: camadaDeAplicacaoReceptora
     * Funcao: converte a mensagem que esta em codigo ASCII para char
     *
     * @param quadro
     * @return void
     */
    private void camadaDeAplicacaoReceptora(int[] quadro) {
        String mensagem;

        StringBuilder sb = new StringBuilder(quadro.length);
        for (int i = 0; i < quadro.length; ++i) {
            if (quadro[i] != -1)
                sb.append((char) quadro[i]);
        }

        mensagem = sb.toString();

        System.out.println("Imprimindo mensagem: " + mensagem);

        aplicacaoReceptora(mensagem);
    } // Fim do metodo camadaDeAplicacaoReceptora

    /**
     * Metodo: aplicacaoReceptora
     * Funcao: exibir o texto decodificado na Area de Texto "mensagemReceptor"
     *
     * @param mensagem eh a mensagem que foi decodificada
     * @return void
     */
    private void aplicacaoReceptora(String mensagem) {
        mensagemReceptor.setText(mensagem); // Seta a mensagem no JTextArea "mensagemReceptor"

        String[] strings = mensagemReceptor.getText().split("\\s+");
        StyledDocument doc = mensagemReceptor.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();

        int lastIndex = 0;

        System.out.println("Imprime tamanho da strings: " + Arrays.toString(strings));

        Random random = new Random();
        for (int i = 0; i < strings.length; i++) {
            // Gera cores randomicas
            StyleConstants.setForeground(set, new Color(random.nextInt(256), random.nextInt(256),
                    random.nextInt(256)));
            // Letras estarao em negrito
            StyleConstants.setBold(set, true);
            doc.setCharacterAttributes(lastIndex, strings[i].length(), set, true);
            // Para cada espaco uma palavra tera uma cor diferente
            lastIndex += strings[i].length() + 1;
        }
    } // Fim do metodo aplicacaoReceptora

    /**
     * Metodo: addItem
     * Funcao: organizar a aplicacao ultilizando o GridBagLayout
     *
     * @param p
     * @param c
     * @param x
     * @param y
     * @param width
     * @param height
     * @param align
     * @return void
     */
    private void addItem(JPanel p, JComponent c, int x, int y, int width, int height, int align) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = width;
        gc.gridheight = height;
        gc.weightx = 100.0;
        gc.weighty = 100.0;
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = align;
        gc.fill = GridBagConstraints.NONE;
        p.add(c, gc);
    } // Fim do metodo addItem
} // Fim da classe Principal
