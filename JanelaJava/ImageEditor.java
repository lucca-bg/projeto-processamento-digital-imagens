import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;


public class ImageEditor extends JFrame {
    private JLabel labelOriginal, labelEditada;
    private BufferedImage imagemOriginal, imagemEditada;

    public ImageEditor() {
        setTitle("Editor de Imagens");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Criando a barra de menus
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem abrirItem = new JMenuItem("Abrir Imagem");
        JMenuItem salvarItem = new JMenuItem("Salvar Imagem");
        JMenuItem sairItem = new JMenuItem("Sair");
        JMenuItem resetItem = new JMenuItem("Reset");
        
        abrirItem.addActionListener(e -> abrirImagem());
        salvarItem.addActionListener(e -> salvarImagem());
        sairItem.addActionListener(e -> System.exit(0));
        resetItem.addActionListener(e -> resetImagem());
        
        menuArquivo.add(abrirItem);
        menuArquivo.add(salvarItem);
        menuArquivo.addSeparator();
        menuArquivo.add(resetItem);
        menuArquivo.add(sairItem);
        menuBar.add(menuArquivo);
        
        // Menu Transformações Geométricas
        JMenu menuTransformacoes = new JMenu("Transformações Geométricas");
        JMenuItem transladarImagem = new JMenuItem("Transladar");
        JMenuItem rotacionarImagem = new JMenuItem("Rotacionar");
        JMenuItem espelharImagem = new JMenuItem("Espelhar");
        JMenuItem aumentarImagem = new JMenuItem("Aumentar");
        JMenuItem diminuirImagem = new JMenuItem("Diminuir");
        
        transladarImagem.addActionListener(e -> translacao());
        rotacionarImagem.addActionListener(e -> rotacao());
        espelharImagem.addActionListener(e -> espelhar());
        aumentarImagem.addActionListener(e -> aumentarImagem());
        diminuirImagem.addActionListener(e -> diminuirImagem());

        menuTransformacoes.add(transladarImagem);
        menuTransformacoes.add(rotacionarImagem);
        menuTransformacoes.add(espelharImagem);
        menuTransformacoes.add(aumentarImagem);
        menuTransformacoes.add(diminuirImagem);
        menuBar.add(menuTransformacoes);
        
        // Menu Filtros
        JMenu menuFiltros = new JMenu("Filtros");
        JMenuItem grayscale = new JMenuItem("Grayscale");
        JMenuItem contrasteBrilho = new JMenuItem("Contraste & Brilho");
        JMenuItem gaussianoItem = new JMenuItem("Filtro Gaussiano");
        JMenuItem filtroMediana = new JMenuItem("Filtro Mediana");

        grayscale.addActionListener(e -> grayscale());
        contrasteBrilho.addActionListener(e -> contrasteBrilho());
        gaussianoItem.addActionListener(e -> filtroGaussiano());
        filtroMediana.addActionListener(e -> filtroMediana());

        JMenuItem ruidoItem = new JMenuItem("Adicionar Ruído");
        ruidoItem.addActionListener(e -> adicionarRuido());
        menuFiltros.add(ruidoItem);
        JMenuItem passaAltaItem = new JMenuItem("Passa Alta");
        passaAltaItem.addActionListener(e -> filtroPassaAlta());
        JMenuItem thresholdItem = new JMenuItem("Threshold");
        thresholdItem.addActionListener(e -> threshold());
        
        menuFiltros.add(grayscale);
        menuFiltros.add(contrasteBrilho);
        menuFiltros.add(gaussianoItem);
        menuFiltros.add(filtroMediana);
        menuFiltros.add(passaAltaItem);
        menuFiltros.add(thresholdItem);
        menuBar.add(menuFiltros);
        
        // Menu Morfologia Matemática
        JMenu menuMorfologia = new JMenu("Morfologia Matemática");
        JMenuItem dilatacaoItem = new JMenuItem("Dilatação");
        dilatacaoItem.addActionListener(e -> dilatacao());
        JMenuItem erosaoItem = new JMenuItem("Erosão");
        erosaoItem.addActionListener(e -> erosao());
        JMenuItem aberturaItem = new JMenuItem("Abertura");
        aberturaItem.addActionListener(e -> abertura());
        JMenuItem fechamentoItem = new JMenuItem("Fechamento");
        fechamentoItem.addActionListener(e -> fechamento());

        menuMorfologia.add(dilatacaoItem);
        menuMorfologia.add(erosaoItem);
        menuMorfologia.add(aberturaItem);
        menuMorfologia.add(fechamentoItem);
        menuBar.add(menuMorfologia);
        
        // Menu Extração de Características
        JMenu menuExtracao = new JMenu("Extração de Características");
        menuExtracao.add(new JMenuItem("Desafio"));
        menuBar.add(menuExtracao);
        
        setJMenuBar(menuBar);
        
        // Criando os painéis de imagem
        JPanel panelImagens = new JPanel(new GridLayout(1, 2, 10, 10));
        labelOriginal = new JLabel("", SwingConstants.CENTER);
        labelEditada = new JLabel("", SwingConstants.CENTER);
        panelImagens.add(labelOriginal);
        panelImagens.add(labelEditada);
        add(panelImagens, BorderLayout.CENTER);
    }

    private void abrirImagem() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                imagemOriginal = ImageIO.read(file);
                labelOriginal.setIcon(new ImageIcon(imagemOriginal.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
                imagemEditada = ImageIO.read(file);
                labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir a imagem!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvarImagem() {
        if (!verificaImagem()) {
            JOptionPane.showMessageDialog(this, "Nenhuma imagem editada para salvar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                ImageIO.write(imagemEditada, "png", file);
                JOptionPane.showMessageDialog(this, "Imagem salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar a imagem!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void translacao() {
        if (!verificaImagem()) {
            JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        JTextField xField = new JTextField();
        JTextField yField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Deslocamento X:"));
        panel.add(xField);
        panel.add(new JLabel("Deslocamento Y:"));
        panel.add(yField);
    
        int result = JOptionPane.showConfirmDialog(null, panel, "Digite os valores de translação", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
    
        int deslocamentoX;
        int deslocamentoY;
        try {
            deslocamentoX = Integer.parseInt(xField.getText());
            deslocamentoY = Integer.parseInt(yField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int largura = imagemEditada.getWidth();
        int altura = imagemEditada.getHeight();
        BufferedImage novaImagem = new BufferedImage(largura, altura, imagemEditada.getType());
    
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                int newX = x + deslocamentoX;
                int newY = y + deslocamentoY;
                
                if (newX >= 0 && newX < largura && newY >= 0 && newY < altura) {
                    novaImagem.setRGB(newX, newY, imagemEditada.getRGB(x, y));
                }
            }
        }
    
        imagemEditada = novaImagem;
        labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
    }
    
    private void rotacao() {
        if (!verificaImagem()) {
            JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        String anguloStr = JOptionPane.showInputDialog(this, "Digite o ângulo de rotação (graus):");
        if (anguloStr == null) {
            return;
        }
        
        double angulo;
        try {
            angulo = Math.toRadians(Double.parseDouble(anguloStr));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int largura = imagemEditada.getWidth();
        int altura = imagemEditada.getHeight();
        BufferedImage novaImagem = new BufferedImage(largura, altura, imagemEditada.getType());
        int centroX = largura / 2;
        int centroY = altura / 2;
    
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                int tempX = x - centroX;
                int tempY = y - centroY;
                int newX = (int) Math.round(tempX * Math.cos(angulo) - tempY * Math.sin(angulo)) + centroX;
                int newY = (int) Math.round(tempX * Math.sin(angulo) + tempY * Math.cos(angulo)) + centroY;
                
                if (newX >= 0 && newX < largura && newY >= 0 && newY < altura) {
                    novaImagem.setRGB(newX, newY, imagemEditada.getRGB(x, y));
                }
            }
        }
    
        imagemEditada = novaImagem;
        labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
    }

    private void espelhar() {
        if (!verificaImagem()) {
            JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        int largura = imagemEditada.getWidth();
        int altura = imagemEditada.getHeight();
        BufferedImage novaImagem = new BufferedImage(largura, altura, imagemEditada.getType());
    
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                novaImagem.setRGB(largura - x - 1, y, imagemEditada.getRGB(x, y));
            }
        }
    
        imagemEditada = novaImagem;
        labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
    }

    private void aumentarImagem() {
        if (!verificaImagem()) {
            JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        String fatorStr = JOptionPane.showInputDialog(this, "Digite o fator de escala (ex: 2 para dobrar):");
        if (fatorStr == null) {
            return;
        }
        
        double fator;
        try {
            fator = Double.parseDouble(fatorStr);
            if (fator <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int largura = imagemEditada.getWidth();
        int altura = imagemEditada.getHeight();
        int novaLargura = (int) (largura * fator);
        int novaAltura = (int) (altura * fator);
        BufferedImage novaImagem = new BufferedImage(novaLargura, novaAltura, imagemEditada.getType());
    
        for (int y = 0; y < novaAltura; y++) {
            for (int x = 0; x < novaLargura; x++) {
                // Calcula a posição do pixel original correspondente na imagem original
                double srcX = x / fator;
                double srcY = y / fator;
    
                int pixelCor = imagemEditada.getRGB((int)srcX, (int)srcY);
                novaImagem.setRGB(x, y, pixelCor);
            }
        }
    
        imagemEditada = novaImagem;
            // Recortar uma área de 300x300 a partir do canto superior esquerdo (0,0)
        int cropWidth = Math.min(300, novaLargura);
        int cropHeight = Math.min(300, novaAltura);
        BufferedImage imagemCortada = imagemEditada.getSubimage(0, 0, cropWidth, cropHeight);

        // Exibir apenas a área cortada
        labelEditada.setIcon(new ImageIcon(imagemCortada));
    }

    private void diminuirImagem() {
        if (!verificaImagem()) {
            JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        String fatorStr = JOptionPane.showInputDialog(this, "Digite o fator de redução (entre 1 e 10, ex: 2 para reduzir à metade, 5 para reduzir a 1/5):");
        if (fatorStr == null) {
            return;
        }
    
        int fator;
        try {
            fator = Integer.parseInt(fatorStr);
            if (fator < 1 || fator > 10) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido! Digite um número entre 1 e 10.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        double escala = 1.0 / fator; // Converte para a fração correta
    
        int largura = imagemEditada.getWidth();
        int altura = imagemEditada.getHeight();
        int novaLargura = (int) (largura * escala);
        int novaAltura = (int) (altura * escala);
        BufferedImage novaImagem = new BufferedImage(novaLargura, novaAltura, imagemEditada.getType());
    
        for (int y = 0; y < novaAltura; y++) {
            for (int x = 0; x < novaLargura; x++) {
                int srcX = (int) (x / escala);
                int srcY = (int) (y / escala);
    
                if (srcX >= largura) srcX = largura - 1;
                if (srcY >= altura) srcY = altura - 1;
    
                novaImagem.setRGB(x, y, imagemEditada.getRGB(srcX, srcY));
            }
        }
    
        imagemEditada = novaImagem;
    
        // Criar uma nova imagem de 300x300 com fundo branco
        BufferedImage imagemFinal = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics g = imagemFinal.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 300, 300);
    
        // Centralizar a imagem reduzida dentro do espaço de 300x300
        int offsetX = (300 - novaLargura) / 2;
        int offsetY = (300 - novaAltura) / 2;
        imagemFinal.getGraphics().drawImage(imagemEditada, offsetX, offsetY, null);
    
        imagemEditada = imagemFinal;
        labelEditada.setIcon(new ImageIcon(imagemEditada));
    }

    private void grayscale(){

        if (!verificaImagem()) {
            JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        int largura = imagemEditada.getWidth();
        int altura = imagemEditada.getHeight();
        BufferedImage novaImagem = new BufferedImage(largura, altura, imagemEditada.getType());
    
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {

                int p = imagemEditada.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
     
                // calculate average
                int avg = (r + g + b) / 3;
     
                // replace RGB value with avg
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;              

                novaImagem.setRGB(x, y, p);
            }
        }
    
        imagemEditada = novaImagem;
        labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));


    }

    private void contrasteBrilho() {
        if (!verificaImagem()) {
            JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        JTextField contrasteField = new JTextField("1.0"); // valor padrão
        JTextField brilhoField = new JTextField("0");
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Contraste (ex: 1.2):"));
        panel.add(contrasteField);
        panel.add(new JLabel("Brilho (ex: 20):"));
        panel.add(brilhoField);
    
        int result = JOptionPane.showConfirmDialog(this, panel, "Ajustar Contraste e Brilho", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;
    
        double contraste;
        int brilho;
        try {
            contraste = Double.parseDouble(contrasteField.getText());
            brilho = Integer.parseInt(brilhoField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int largura = imagemEditada.getWidth();
        int altura = imagemEditada.getHeight();
        BufferedImage novaImagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
    
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                int rgb = imagemEditada.getRGB(x, y);
    
                int a = (rgb >> 24) & 0xff;
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
    
                // Aplica a fórmula no intervalo de cada cor
                r = clamp((int)(contraste * r + brilho));
                g = clamp((int)(contraste * g + brilho));
                b = clamp((int)(contraste * b + brilho));
    
                int novoRGB = (a << 24) | (r << 16) | (g << 8) | b;
                novaImagem.setRGB(x, y, novoRGB);
            }
        }
    
        imagemEditada = novaImagem;
        labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
    }
    
    private void filtroGaussiano() {
        if (!verificaImagem()) {
            JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        // Máscara Gaussiana 3x3 (normalizada, soma = 16)
        int[][] kernel = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
        };
        int fatorDivisao = 16;
    
        int largura = imagemEditada.getWidth();
        int altura = imagemEditada.getHeight();
        BufferedImage novaImagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
    
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                int somaR = 0, somaG = 0, somaB = 0;
    
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = imagemEditada.getRGB(x + kx, y + ky);
                        int r = (pixel >> 16) & 0xff;
                        int g = (pixel >> 8) & 0xff;
                        int b = pixel & 0xff;
    
                        int peso = kernel[ky + 1][kx + 1];
                        somaR += r * peso;
                        somaG += g * peso;
                        somaB += b * peso;
                    }
                }
    
                int novoR = clamp(somaR / fatorDivisao);
                int novoG = clamp(somaG / fatorDivisao);
                int novoB = clamp(somaB / fatorDivisao);
                int a = (imagemEditada.getRGB(x, y) >> 24) & 0xff;
    
                int novoRGB = (a << 24) | (novoR << 16) | (novoG << 8) | novoB;
                novaImagem.setRGB(x, y, novoRGB);
            }
        }
    
        imagemEditada = novaImagem;
        labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
    }

    private void filtroMediana() {
    if (!verificaImagem()) {
        JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int largura = imagemEditada.getWidth();
    int altura = imagemEditada.getHeight();
    BufferedImage novaImagem = new BufferedImage(largura, altura, imagemEditada.getType());

    for (int y = 1; y < altura - 1; y++) {
        for (int x = 1; x < largura - 1; x++) {
            int[] vizinhosR = new int[9];
            int[] vizinhosG = new int[9];
            int[] vizinhosB = new int[9];
            int index = 0;

            for (int ky = -1; ky <= 1; ky++) {
                for (int kx = -1; kx <= 1; kx++) {
                    int pixel = imagemEditada.getRGB(x + kx, y + ky);
                    vizinhosR[index] = (pixel >> 16) & 0xff;
                    vizinhosG[index] = (pixel >> 8) & 0xff;
                    vizinhosB[index] = pixel & 0xff;
                    index++;
                }
            }

            Arrays.sort(vizinhosR);
            Arrays.sort(vizinhosG);
            Arrays.sort(vizinhosB);

            int r = vizinhosR[4]; // mediana (posição central após ordenação)
            int g = vizinhosG[4];
            int b = vizinhosB[4];
            int a = (imagemEditada.getRGB(x, y) >> 24) & 0xff;

            int novoRGB = (a << 24) | (r << 16) | (g << 8) | b;
            novaImagem.setRGB(x, y, novoRGB);
        }
    }

    imagemEditada = novaImagem;
    labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
    }

    
private void adicionarRuido() {
    if (!verificaImagem()) {
        JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String intensidadeStr = JOptionPane.showInputDialog(this, "Digite a intensidade do ruído (1-100%):");
    if (intensidadeStr == null) return;

    int intensidade;
    try {
        intensidade = Integer.parseInt(intensidadeStr);
        if (intensidade < 1 || intensidade > 100) throw new NumberFormatException();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Valor inválido! Digite um número entre 1 e 100.", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int largura = imagemOriginal.getWidth();
    int altura = imagemOriginal.getHeight();
    BufferedImage novaImagem = new BufferedImage(largura, altura, imagemOriginal.getType());

    // Copia a imagem original
    for (int x = 0; x < largura; x++) {
        for (int y = 0; y < altura; y++) {
            novaImagem.setRGB(x, y, imagemOriginal.getRGB(x, y));
        }
    }

    // Adiciona o ruído aleatório
    int totalPixels = largura * altura;
    int pixelsComRuido = totalPixels * intensidade / 100;
    Random rand = new Random();

    for (int i = 0; i < pixelsComRuido; i++) {
        int x = rand.nextInt(largura);
        int y = rand.nextInt(altura);
        int cor = rand.nextBoolean() ? Color.WHITE.getRGB() : Color.BLACK.getRGB();
        novaImagem.setRGB(x, y, cor);
    }

    imagemEditada = novaImagem;
    labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
}


    // Garante que os valores fiquem entre 0 e 255
    private int clamp(int valor) {
        return Math.max(0, Math.min(255, valor));
    }
    
    
    private boolean verificaImagem(){
        if(imagemEditada == null){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageEditor().setVisible(true));
    }
private void filtroPassaAlta() {
    if (!verificaImagem()) {
        JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Máscara passa-alta clássica 3x3
    int[][] kernel = {
        { -1, -1, -1 },
        { -1,  8, -1 },
        { -1, -1, -1 }
    };

    int largura = imagemEditada.getWidth();
    int altura = imagemEditada.getHeight();
    BufferedImage novaImagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);

    for (int y = 1; y < altura - 1; y++) {
        for (int x = 1; x < largura - 1; x++) {
            int somaR = 0, somaG = 0, somaB = 0;

            for (int ky = -1; ky <= 1; ky++) {
                for (int kx = -1; kx <= 1; kx++) {
                    int pixel = imagemEditada.getRGB(x + kx, y + ky);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;

                    int peso = kernel[ky + 1][kx + 1];
                    somaR += r * peso;
                    somaG += g * peso;
                    somaB += b * peso;
                }
            }

            int novoR = clamp(somaR);
            int novoG = clamp(somaG);
            int novoB = clamp(somaB);
            int a = (imagemEditada.getRGB(x, y) >> 24) & 0xff;

            int novoRGB = (a << 24) | (novoR << 16) | (novoG << 8) | novoB;
            novaImagem.setRGB(x, y, novoRGB);
        }
    }

    imagemEditada = novaImagem;
    labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
}
private void threshold() {
    if (!verificaImagem()) {
        JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String limiarStr = JOptionPane.showInputDialog(this, "Digite o valor do limiar (0-255):", "128");
    if (limiarStr == null) return;

    int limiar;
    try {
        limiar = Integer.parseInt(limiarStr);
        if (limiar < 0 || limiar > 255) throw new NumberFormatException();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int largura = imagemEditada.getWidth();
    int altura = imagemEditada.getHeight();
    BufferedImage novaImagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);

    for (int y = 0; y < altura; y++) {
        for (int x = 0; x < largura; x++) {
            int pixel = imagemEditada.getRGB(x, y);
            int a = (pixel >> 24) & 0xff;
            int r = (pixel >> 16) & 0xff;
            int g = (pixel >> 8) & 0xff;
            int b = pixel & 0xff;
            // Calcula o valor de cinza
            int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);
            int bin = (gray >= limiar) ? 255 : 0;
            int novoPixel = (a << 24) | (bin << 16) | (bin << 8) | bin;
            novaImagem.setRGB(x, y, novoPixel);
        }
    }

    imagemEditada = novaImagem;
    labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
}
private void dilatacao() {
    if (!verificaImagem()) {
        JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int largura = imagemEditada.getWidth();
    int altura = imagemEditada.getHeight();
    BufferedImage novaImagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);

    // Estruturante 3x3
    for (int y = 1; y < altura - 1; y++) {
        for (int x = 1; x < largura - 1; x++) {
            int max = 0;
            int a = (imagemEditada.getRGB(x, y) >> 24) & 0xff;
            for (int ky = -1; ky <= 1; ky++) {
                for (int kx = -1; kx <= 1; kx++) {
                    int pixel = imagemEditada.getRGB(x + kx, y + ky);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;
                    int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                    if (gray > max) max = gray;
                }
            }
            int novoPixel = (a << 24) | (max << 16) | (max << 8) | max;
            novaImagem.setRGB(x, y, novoPixel);
        }
    }

    imagemEditada = novaImagem;
    labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
}
private void erosao() {
    if (!verificaImagem()) {
        JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int largura = imagemEditada.getWidth();
    int altura = imagemEditada.getHeight();
    BufferedImage novaImagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);

    // Estruturante 3x3
    for (int y = 1; y < altura - 1; y++) {
        for (int x = 1; x < largura - 1; x++) {
            int min = 255;
            int a = (imagemEditada.getRGB(x, y) >> 24) & 0xff;
            for (int ky = -1; ky <= 1; ky++) {
                for (int kx = -1; kx <= 1; kx++) {
                    int pixel = imagemEditada.getRGB(x + kx, y + ky);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;
                    int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                    if (gray < min) min = gray;
                }
            }
            int novoPixel = (a << 24) | (min << 16) | (min << 8) | min;
            novaImagem.setRGB(x, y, novoPixel);
        }
    }

    imagemEditada = novaImagem;
    labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
}
private void abertura() {
    if (!verificaImagem()) {
        JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int largura = imagemEditada.getWidth();
    int altura = imagemEditada.getHeight();

    // --- Erosão ---
    BufferedImage imagemErodida = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
    for (int y = 1; y < altura - 1; y++) {
        for (int x = 1; x < largura - 1; x++) {
            int min = 255;
            int a = (imagemEditada.getRGB(x, y) >> 24) & 0xff;
            for (int ky = -1; ky <= 1; ky++) {
                for (int kx = -1; kx <= 1; kx++) {
                    int pixel = imagemEditada.getRGB(x + kx, y + ky);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;
                    int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                    if (gray < min) min = gray;
                }
            }
            int novoPixel = (a << 24) | (min << 16) | (min << 8) | min;
            imagemErodida.setRGB(x, y, novoPixel);
        }
    }

    // --- Dilatação ---
    BufferedImage imagemAberta = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
    for (int y = 1; y < altura - 1; y++) {
        for (int x = 1; x < largura - 1; x++) {
            int max = 0;
            int a = (imagemErodida.getRGB(x, y) >> 24) & 0xff;
            for (int ky = -1; ky <= 1; ky++) {
                for (int kx = -1; kx <= 1; kx++) {
                    int pixel = imagemErodida.getRGB(x + kx, y + ky);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;
                    int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                    if (gray > max) max = gray;
                }
            }
            int novoPixel = (a << 24) | (max << 16) | (max << 8) | max;
            imagemAberta.setRGB(x, y, novoPixel);
        }
    }

    imagemEditada = imagemAberta;
    labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
}
private void fechamento() {
    if (!verificaImagem()) {
        JOptionPane.showMessageDialog(this, "Nenhuma imagem carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int largura = imagemEditada.getWidth();
    int altura = imagemEditada.getHeight();

    // --- Dilatação ---
    BufferedImage imagemDilatada = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
    for (int y = 1; y < altura - 1; y++) {
        for (int x = 1; x < largura - 1; x++) {
            int max = 0;
            int a = (imagemEditada.getRGB(x, y) >> 24) & 0xff;
            for (int ky = -1; ky <= 1; ky++) {
                for (int kx = -1; kx <= 1; kx++) {
                    int pixel = imagemEditada.getRGB(x + kx, y + ky);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;
                    int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                    if (gray > max) max = gray;
                }
            }
            int novoPixel = (a << 24) | (max << 16) | (max << 8) | max;
            imagemDilatada.setRGB(x, y, novoPixel);
        }
    }

    // --- Erosão ---
    BufferedImage imagemFechada = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
    for (int y = 1; y < altura - 1; y++) {
        for (int x = 1; x < largura - 1; x++) {
            int min = 255;
            int a = (imagemDilatada.getRGB(x, y) >> 24) & 0xff;
            for (int ky = -1; ky <= 1; ky++) {
                for (int kx = -1; kx <= 1; kx++) {
                    int pixel = imagemDilatada.getRGB(x + kx, y + ky);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;
                    int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                    if (gray < min) min = gray;
                }
            }
            int novoPixel = (a << 24) | (min << 16) | (min << 8) | min;
            imagemFechada.setRGB(x, y, novoPixel);
        }
    }

    imagemEditada = imagemFechada;
    labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
}
private void resetImagem() {
    if (imagemOriginal == null) {
        JOptionPane.showMessageDialog(this, "Nenhuma imagem original carregada!", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int largura = imagemOriginal.getWidth();
    int altura = imagemOriginal.getHeight();
    BufferedImage copia = new BufferedImage(largura, altura, imagemOriginal.getType());
    Graphics g = copia.getGraphics();
    g.drawImage(imagemOriginal, 0, 0, null);
    g.dispose();
    imagemEditada = copia;
    labelEditada.setIcon(new ImageIcon(imagemEditada.getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
}
}
