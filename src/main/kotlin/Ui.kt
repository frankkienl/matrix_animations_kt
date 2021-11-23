import java.awt.*
import java.io.File
import javax.swing.*
import kotlin.system.exitProcess


fun openUI() {
    try {
        // Set cross-platform Java L&F (also called "Metal")
        UIManager.setLookAndFeel(
            //UIManager.getCrossPlatformLookAndFeelClassName()
            UIManager.getSystemLookAndFeelClassName()
        )
    } catch (e: Exception) {
        //ignore
    }
    Ui()
}

object State {
    var myFiles: List<MyFile>? = null
    var openedFolder: File? = null
    var selectedFile: MyFile? = null
    var currentImage: Image? = null
    var currentPreviewImage: Image? = null
    var previewScale: Int = 30 //times bigger than actual
    var drawLines: Boolean = true
}

class Ui {
    init {
        initUI()
    }

    private lateinit var jFrame: JFrame
    private lateinit var statusLabel: Label
    private lateinit var jMenuBar: JMenuBar
    private lateinit var jListFiles: JList<MyFile>
    private lateinit var jListModel: DefaultListModel<MyFile>
    private lateinit var jImagePanel: JPanel

    private fun initUI() {
        jFrame = JFrame()
        //Frame
        jFrame.title = "Matrix animation"
        jFrame.size = Dimension(640, 480)
        jFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        //Menu
        initMenu()
        jFrame.jMenuBar = jMenuBar
        //Layout
        jFrame.layout = BorderLayout()
        statusLabel = Label("Load animation project folder")
        jFrame.add(statusLabel, BorderLayout.SOUTH)
        //List
        jListFiles = JList<MyFile>()
        jListFiles.selectionMode = ListSelectionModel.SINGLE_SELECTION
        jListFiles.layoutOrientation = JList.VERTICAL
        jListFiles.addListSelectionListener { event ->
            if (!event.valueIsAdjusting) {
                val index = jListFiles.selectedIndex
                State.myFiles?.let {
                    selectedFile(it[index])
                }
            }
        }
        jListModel = DefaultListModel<MyFile>()
        jListFiles.model = jListModel
        val jScrollPaneListFiles = JScrollPane(jListFiles)
        jFrame.add(jScrollPaneListFiles, BorderLayout.WEST)
        //image preview
        jImagePanel = MyImagePanel()
        jFrame.add(JScrollPane(jImagePanel), BorderLayout.CENTER)
        //Show
        jFrame.isVisible = true
    }

    private fun initMenu() {
        jMenuBar = JMenuBar()
        val jMenuFile = JMenu(S.file)
        val itemOpen = JMenuItem(S.openDot)
        itemOpen.addActionListener {
            openFolder()
        }
        jMenuFile.add(itemOpen)
        val itemSave = JMenuItem(S.save)
        jMenuFile.add(itemSave)
        val itemExit = JMenuItem(S.exit)
        itemExit.addActionListener {
            exitApp()
        }
        jMenuFile.add(itemExit)
        jMenuBar.add(jMenuFile)
        val jMenuHelp = JMenu(S.help)
        val itemAbout = JMenuItem(S.about)
        itemAbout.addActionListener {
            JOptionPane.showMessageDialog(null, S.aboutDesc)
        }
        jMenuHelp.add(itemAbout)
        jMenuBar.add(jMenuHelp)
    }

    private fun openFolder() = with(State) {
        val fc = JFileChooser()
        fc.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        val status = fc.showDialog(null, S.open)
        if (status == JFileChooser.ERROR_OPTION) {
            JOptionPane.showMessageDialog(null, S.openFolderError)
        }
        if (status == JFileChooser.APPROVE_OPTION) {
            openedFolder = fc.selectedFile
            openedFolder()
        }
    }

    private fun openedFolder() = with(State) {
        openedFolder?.let { safeOpenedFolder ->
            val files = safeOpenedFolder.listFiles { file -> file.name.endsWith(".png") }
            files?.sortBy { file -> file.name }
            myFiles = files?.map { file -> MyFile(file) }
            myFiles?.let { safeMyFiles ->
                jListFiles.setListData(safeMyFiles.toTypedArray())
                jFrame.invalidate()
            }
        }
    }

    private fun selectedFile(selected: MyFile) = with(State) {
        selectedFile = selected
        if (selected.frame == null) {
            val leds = parseFile(selected.file)
            leds?.let {
                val frame = Frame(it)
                selected.frame = frame
            }
        }
        refreshPreview(selected)
    }

    private fun refreshPreview(selected: MyFile) = with(State) {
        val image = fileToImage(selected.file) ?: return
        currentImage = image
        //scale up
        currentPreviewImage = scaleUpImage(image, previewScale)
        //
        jImagePanel.invalidate()
        jImagePanel.repaint()
    }

    private fun exitApp() {
        val confirmation = JOptionPane.showConfirmDialog(null, S.closeAppConfirmation)
        if (confirmation == JOptionPane.YES_OPTION) {
            exitProcess(0)
        }
    }
}

class MyImagePanel : JPanel() {
    init {
        border = BorderFactory.createLineBorder(Color.black)
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(250, 200)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        //
        with(State) {
            currentPreviewImage?.let { safeImage ->
                g.drawImage(safeImage, 0, 0, null, null)
            }
        }
    }
}

data class MyFile(val file: File, var frame: Frame? = null) {
    override fun toString(): String {
        return file.name
    }
}