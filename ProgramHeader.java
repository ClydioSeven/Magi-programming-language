public class ProgramHeader{
    /*
    private int p_type=1; 
    private int p_flags=5;
    private long p_offset=0;
    private long p_vaddr=65536;
    private long p_paddr=0;
    private long p_filesz=0;
    private long p_memsz=0;
    private long p_align=0;
    */
    private byte[] header = {};
    public ProgramHeader(long size){
        this.header = getHeader(toBytes(size));
    }
    private byte[] toBytes(long size) {
        return new byte[] {
            (byte)(size),
            (byte)(size >> 8),
            (byte)(size >> 16),
            (byte)(size >> 24),
            (byte)(size >> 32),
            (byte)(size >> 40),
            (byte)(size >> 48),
            (byte)(size >> 56)
        };
    }   

    private byte[] getHeader(byte[] bytes) {
        return new byte[] {
            0x01, 0x00, 0x00, 0x00,
            0x05, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,  
            0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,  
            bytes[0],bytes[1],bytes[2],bytes[3],bytes[4],bytes[5],bytes[6],bytes[7],
            bytes[0],bytes[1],bytes[2],bytes[3],bytes[4],bytes[5],bytes[6],bytes[7],
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };
    }
    public byte[] getHeaderBytes() {
        return this.header;   
    }
}