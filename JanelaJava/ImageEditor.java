import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import java.awt.geom.AffineTransform;

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
        
        abrirItem.addActionListener(e -> abrirImagem());
        salvarItem.addActionListener(e -> salvarImagem());
        sairItem.addActionListener(e -> System.exit(0));
        
        menuArquivo.add(abrirItem);
        menuArquivo.add(salvarItem);
        menuArquivo.addSeparator();
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
        menuFiltros.add(new JMenuItem("Grayscale"));
        menuFiltros.add(new JMenuItem("Passa Baixa"));
        menuFiltros.add(new JMenuItem("Passa Alta"));
        menuFiltros.add(new JMenuItem("Threshold"));
        menuBar.add(menuFiltros);
        
        // Menu Morfologia Matemática
        JMenu menuMorfologia = new JMenu("Morfologia Matemática");
        menuMorfologia.add(new JMenuItem("Dilatação"));
        menuMorfologia.add(new JMenuItem("Erosão"));
        menuMorfologia.add(new JMenuItem("Abertura"));
        menuMorfologia.add(new JMenuItem("Fechamento"));
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
    

    private boolean verificaImagem(){
        if(imagemEditada == null){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageEditor().setVisible(true));
    }
}
