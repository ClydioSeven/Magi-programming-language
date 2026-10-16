public class ElfHeader{
    public ElfHeader(){
        /*
        byte ELFMAG0 = 0x7f;
        byte ELFMAG1 = 'E';
        byte ELFMAG2 = 'L';
        byte ELFMAG3 = 'F';
        byte EI_CLASS = 2;
        byte EI_DATA=1;
        byte EI_VERSION=1;
        byte EI_PAD=0;
        byte p=0;
        byte M='M';
        byte A='A';
        byte G='G';
        byte I='I';
        short e_type = 2;
        short e_machine = 62;
        int e_version = 1;
        long e_entry = 136;
        long e_phoff = 64;
        long e_shoff = 0;
        int e_flags = 0;
        short e_ehsize = 64;
        short e_phentsize = 56;
        short e_phnum = 1;
        short e_shentsize = 64;
        short e_shnum = 0;
        short e_shstrndx = 0;
        */
    }
    private byte[] MagiSpell(){
        byte[] arr = {0x49,0x14,0x2B,0x3D,0x49,0x4A,0x14,0x2E,0x45,0x48,0x41,0x3E,0x3D,0x4C,0x4C,0x15};
        return arr;
    }
    public byte[] getHeader() {
        return new byte[] {

            // e_ident (16 bytes)
            0x7F, 0x45, 0x4C, 0x46,
            0x02,
            0x01,
            0x01,
            0x00,
            0x00, 0x00, 0x00, 0x00,
            0x4D, 0x41, 0x47, 0x49,

            // e_type = 2
            0x02, 0x00,

            // e_machine = 62
            0x3E, 0x00,

            // e_version = 1
            0x01, 0x00, 0x00, 0x00,

            // e_entry = 0x10088
            (byte)0x88, 0x00, 0x01, 0x00,
            0x00, 0x00, 0x00, 0x00,

            // e_phoff = 64
            0x40, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,

            // e_shoff = 0
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,

            // e_flags = 0
            0x00, 0x00, 0x00, 0x00,

            // e_ehsize = 64
            0x40, 0x00,

            // e_phentsize = 56
            0x38, 0x00,

            // e_phnum = 1
            0x01, 0x00,

            // e_shentsize = 64
            0x40, 0x00,

            // e_shnum = 0
            0x00, 0x00,

            // e_shstrndx = 0
            0x00, 0x00
        };
    }
    
}
