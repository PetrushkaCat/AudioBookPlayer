package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.books_scanner

interface BooksScannerComponent {

    //val scanState: StateFlow<ScanState>

    fun scan()

    /*sealed interface ScanState {
        object Scanning: ScanState
        object NotScanning: ScanState
    }*/
}