export default function ImageBody({image, alt}) {
    return (
        <div className="image">
            <img src={image} alt={alt} />
        </div>
    )
}