package br.com.crud.view.consulta;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.towel.el.annotation.AnnotationResolver;
import com.towel.swing.table.ObjectTableModel;
import com.towel.swing.table.TableFilter;

import br.com.crud.dao.ClienteDAO;
import br.com.crud.model.Cliente;
import br.com.crud.view.cadastro.TelaCliente;
import br.com.crud.view.cadastro.TelaPreVenda;

import javax.swing.ScrollPaneConstants;
import java.awt.SystemColor;
import java.awt.Color;

public class TelaConsultaCliente extends JDialog {
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel lbBusca = null;
	private JTextField txtBusca = null;
	private JScrollPane jScrollPane = null;
	private JTable table = null;
	private JButton btnFechar = null;
	private JLabel lbInformacao = null;
	private String texto = null;
	private String retorno = null;
	// objectablemodel � um modelo de jtable
	private ObjectTableModel<Cliente> model = null;// modelo da JTable
	// tablefilter � a classe que faz filtragens na jtable
	private TableFilter filtro = null;// auto filtro da Jtable
	private JButton btnCad = null;
	private int total = 0;
	private JLabel lbTotal = null;
	private JCheckBox checkAvancada = null;
	private JPanel painel;
	private JLabel lbConsultaCliente;

	/**
	 * @wbp.parser.constru
	 */
	public TelaConsultaCliente(TelaPreVenda telaPreVenda) {
		super(telaPreVenda);
		initialize();
	}
	
	public TelaConsultaCliente(TelaCliente telaCliente) {
		super(telaCliente);
		initialize();
	}

	public TelaConsultaCliente() {// construtor do formulario
		super();
		initialize();// metodo que inicializa os componentes
	}

	@SuppressWarnings("serial")
	private void initialize() {
		// inicializa��o dos componentes
		this.setTitle("Consulta de Cliente");
		this.setSize(695, 436);
		// tamanho da tela
		this.setResizable(false);
		// n�o redimensionar
		this.setModal(true);
		// n�o mecher na de tras enquanto esta tiver aberta
		this.setContentPane(getJContentPane());
		// passando para o formulario o painel principal
		this.setLocationRelativeTo(getOwner());
		// iniciar no meio da tela o formulario
		// o metodo de baixo � para receber os dados da tela que chamou a
		// consulta
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowOpened(java.awt.event.WindowEvent e) {
				setTexto(getTexto());
				if (getTexto() != null) {
					// casso a tela que chamou a consulta tenha passado um
					// texto, faz a consulta com o texto
					txtBusca.setText(getTexto());
					carregaLista(getTexto());// chama o metodo
				}
			}
		});
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "ESCAPE");
		getRootPane().getActionMap().put("ESCAPE", new AbstractAction("ESCAPE") {
			@Override
			public void actionPerformed(ActionEvent evt) {
				dispose();
			}
		});
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F11"), "F11");
		getRootPane().getActionMap().put("F11", new AbstractAction("F11") {
			@Override
			public void actionPerformed(ActionEvent evt) {
				checkAvancada.setSelected(!checkAvancada.isSelected());
				carregaLista(txtBusca.getText());
			}
		});
		getJContentPane().add(getPainel());
	}

	@SuppressWarnings("unchecked")
	private void carregaLista(String texto) {
		ClienteDAO dao = new ClienteDAO();
		try {
			// faz a consulta
			List lista = dao.buscaSimples(texto);
			carregaTable(lista);// chama o metodo que carrega a tabela
		} catch (Exception busca) {
			busca.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void carregaTable(List lista) {
		total = lista.size();
		// necessario para criar o modelo da JTable
		AnnotationResolver resolver = new AnnotationResolver(Cliente.class);
		// instancia o modelo da tabela, com os campos que ir� aparecer separado
		// por virgula
		model = new ObjectTableModel<Cliente>(resolver, "codigo,razaoSocial,cpf");
		model.setData(lista);
		// passa a lista para o modelo
		filtro = new TableFilter(table.getTableHeader(), model);
		// passa o modelo para o filtro
		table.setModel(filtro);
		// passa o filtro para a JTable
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// redimensionar a tabela
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(497);
		if (table.getRowCount() > 0) {
			table.changeSelection(0, 0, false, false);
			table.requestFocus();
			lbTotal.setText("Exibindo registro " + (table.getSelectedRow() + 1) + " de " + total + ".");
		}
	}

	private JPanel getJContentPane() {
		// metodo que constroi o painel
		if (jContentPane == null) {
			lbInformacao = new JLabel();

			lbInformacao.setBounds(new Rectangle(6, 327, 460, 16));
			lbInformacao.setText("Enter/Dois cliques para selecionar");
			lbBusca = new JLabel();
			lbBusca.setBounds(new Rectangle(10, 84, 40, 20));
			lbBusca.setText("Buscar:");
			lbTotal = new JLabel();
			lbTotal.setHorizontalAlignment(SwingConstants.RIGHT);
			lbTotal.setBounds(new Rectangle(422, 327, 260, 16));
			lbTotal.setText("");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(lbBusca, null);
			jContentPane.add(lbTotal, null);
			jContentPane.add(getTxtBusca(), null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getBtnFechar(), null);
			jContentPane.add(lbInformacao, null);
			jContentPane.add(getBtnCad(), null);
		}
		return jContentPane;
	}

	private JTextField getTxtBusca() {
		if (txtBusca == null) {
			txtBusca = new JTextField();
			txtBusca.setToolTipText("Buscar Cliente");
			txtBusca.setBounds(new Rectangle(56, 84, 369, 20));
			txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				@SuppressWarnings("static-access")
				// se digitar enter chama o metodo que faz busca
				public void keyPressed(java.awt.event.KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						carregaLista(txtBusca.getText());
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						table.requestFocus();
					}
				}
			});
		}
		return txtBusca;
	}

	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			jScrollPane.setBounds(new Rectangle(10, 123, 672, 193));
			jScrollPane.setViewportView(getTable());
		}
		return jScrollPane;
	}

	private JTable getTable() {
		if (table == null) {
			table = new JTable();
			table.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyReleased(java.awt.event.KeyEvent e) {

					lbTotal.setText("Exibindo registro " + (table.getSelectedRow() + 1) + " de " + total + ".");

				}

				@Override
				@SuppressWarnings("static-access")
				// caso de enter chama o metodo que faz o retorno para tela de
				// tras
				public void keyPressed(java.awt.event.KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						selecionar();
					} else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_B || e.getKeyCode() == KeyEvent.VK_C || e.getKeyCode() == KeyEvent.VK_D 
							|| e.getKeyCode() == KeyEvent.VK_E || e.getKeyCode() == KeyEvent.VK_F || e.getKeyCode() == KeyEvent.VK_G || e.getKeyCode() == KeyEvent.VK_H 
							|| e.getKeyCode() == KeyEvent.VK_I || e.getKeyCode() == KeyEvent.VK_J || e.getKeyCode() == KeyEvent.VK_K || e.getKeyCode() == KeyEvent.VK_L 
							|| e.getKeyCode() == KeyEvent.VK_M || e.getKeyCode() == KeyEvent.VK_N || e.getKeyCode() == KeyEvent.VK_O || e.getKeyCode() == KeyEvent.VK_P 
							|| e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_R || e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_T 
							|| e.getKeyCode() == KeyEvent.VK_U || e.getKeyCode() == KeyEvent.VK_V || e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_Y 
							|| e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_Z) {
						txtBusca.setText(e.getKeyChar() + "");
						txtBusca.requestFocus();
					}
				}
			});
			// caso de um clique duplo na linha da tabela, chama o metodo que
			// retorna
			table.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					lbTotal.setText("Exibindo registro " + (table.getSelectedRow() + 1) + " de " + total + ".");
					if (e.getClickCount() == 2) {
						selecionar();
					}
				}
			});
		}
		return table;
	}

	private void selecionar() {
		// metodo que faz o retorno.. pega o codigo
		if (table.getRowCount() > 0) {
			if (filtro.isFiltering() || filtro.isSorting()) {
				String codigo = model.getValue(filtro.getModelRow(table.getSelectedRow())).getCodigo().toString();
				setRetorno(codigo);
				dispose();
			} else {
				String codigo = model.getValue(table.getSelectedRow()).getCodigo().toString();
				setRetorno(codigo);
				dispose();
				
				
			}
		}
	}

	private JButton getBtnFechar() {
		if (btnFechar == null) {
			btnFechar = new JButton();
			btnFechar.setToolTipText("Fechar tela");
			btnFechar.setIcon(new ImageIcon(TelaConsultaCliente.class.getResource("/br/com/crud/img/icons8-fechar-janela-30.png")));
			btnFechar.setFont(new Font("Tahoma", Font.PLAIN, 17));
			btnFechar.setBounds(new Rectangle(517, 354, 150, 35));
			// btnFechar.setIcon(new
			// ImageIcon(TelaConsultaCliente.class.getResource("/icones/alertacancela.png")));
			btnFechar.setText("Fechar");
			btnFechar.setMnemonic('F');
			btnFechar.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return btnFechar;
	}

	private JButton getBtnCad() {
		if (btnCad == null) {
			btnCad = new JButton();
			btnCad.setToolTipText("Cadastrar Cliente");
			btnCad.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnCad.setBounds(new Rectangle(542, 81, 125, 27));
			// btnCad.setIcon(new
			// ImageIcon(getClass().getResource("/icones/cadastro.png")));
			btnCad.setText("Cadastro");
			btnCad.setMnemonic('C');
			btnCad.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					txtBusca.requestFocus();
					TelaCliente tela = new TelaCliente(TelaConsultaCliente.this);
					tela.setNovo(true);
					tela.setVisible(true);
					if (tela.getCadastro() != null) {
						setRetorno(tela.getCadastro().getCodigo().toString());
						dispose();
					}
					carregaLista(txtBusca.getText());
				}
			});
		}
		return btnCad;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTexto() {
		return texto;
	}

	public void setRetorno(String retorno) {
		this.retorno = retorno;
	}

	public String getRetorno() {
		return retorno;
	}

	private JPanel getPainel() {
		if (painel == null) {
			painel = new JPanel();
			painel.setLayout(null);
			painel.setBackground(SystemColor.textHighlight);
			painel.setBounds(0, 0, 689, 60);
			painel.add(getLbConsultaCliente());
		}
		return painel;
	}
	private JLabel getLbConsultaCliente() {
		if (lbConsultaCliente == null) {
			lbConsultaCliente = new JLabel("Consulta de Clientes");
			lbConsultaCliente.setForeground(Color.WHITE);
			lbConsultaCliente.setFont(new Font("Tahoma", Font.PLAIN, 32));
			lbConsultaCliente.setBounds(195, 0, 286, 60);
		}
		return lbConsultaCliente;
	}
}
