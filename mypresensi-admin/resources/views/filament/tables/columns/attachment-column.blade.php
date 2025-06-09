<div>
    @if($getState())
        @if($isImage())
            <div class="w-12 h-12 rounded-lg overflow-hidden">
                <img 
                    src="{{ $getAttachmentUrl() }}" 
                    alt="Attachment"
                    class="w-full h-full object-cover"
                />
            </div>
        @else
            <a 
                href="{{ $getAttachmentUrl() }}" 
                target="_blank"
                class="inline-flex items-center space-x-1 text-primary-600 hover:text-primary-500"
            >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z"></path>
                </svg>
                <span>{{ $getFileName() }}</span>
            </a>
        @endif
    @endif
</div>